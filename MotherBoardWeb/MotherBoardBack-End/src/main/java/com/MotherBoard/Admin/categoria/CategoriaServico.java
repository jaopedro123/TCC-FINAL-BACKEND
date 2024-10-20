package com.MotherBoard.Admin.categoria;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.Categoria;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaServico {
	public static final int ROOT_CATEGORIAS_POR_PAGINA = 3;	
	@Autowired
	private CategoriaRepository repo;
	
	public List<Categoria> listByPage(CategoriaPaginaInfo paginaInfo, int pageNum, String sortDir, String keyword) {
		Sort sort = Sort.by("nome");

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}

		Pageable pageable = PageRequest.of(pageNum -1, ROOT_CATEGORIAS_POR_PAGINA, sort);

		Page<Categoria> pageCategorias = null;
		if (keyword != null && !keyword.isEmpty()) {
			pageCategorias = repo.pesquisar(keyword, pageable);
		} else {
			pageCategorias = repo.findRootCategorias(pageable);
		}
		List<Categoria> rootCategorias = pageCategorias.getContent();

		paginaInfo.setTotalElementos(pageCategorias.getTotalElements());
		paginaInfo.setTotalPaginas(pageCategorias.getTotalPages());

		if (keyword != null && !keyword.isEmpty()) {
			List<Categoria> resultadoPesquisa = pageCategorias.getContent();	
			for (Categoria categoria : resultadoPesquisa) {
				categoria.setHasChildren(categoria.getFilho().size() > 0);
			}
			return resultadoPesquisa;
		} else {
			return listarHierarchicalCategorias(rootCategorias, sortDir);
		}	
	}

	private List<Categoria> listarHierarchicalCategorias(List<Categoria> rootCategorias, String sortDir) {
		List<Categoria> hierarchicalCategorias = new  ArrayList<>();
		
		for (Categoria rootCategoria : rootCategorias) {
			hierarchicalCategorias.add(Categoria.copiarTudo(rootCategoria));

			Set<Categoria> filho = sortSubCategoria(rootCategoria.getFilho(), sortDir);
			
			for (Categoria subCategoria : filho) {
				String nome = "--" + subCategoria.getNome();
				hierarchicalCategorias.add(Categoria.copiarTudo(subCategoria, nome));

				listarSubHierarchicalCategorias(hierarchicalCategorias, subCategoria, sortDir);
			}
		}

		return hierarchicalCategorias;
	}

	private void listarSubHierarchicalCategorias(List<Categoria> hierarchicalCategorias, Categoria pai, String sortDir) {
		Set<Categoria> filho = sortSubCategoria(pai.getFilho(), sortDir);
		
		for (Categoria subCategoria : filho) {
			String nome = "----" + subCategoria.getNome();
		
			hierarchicalCategorias.add(Categoria.copiarTudo(subCategoria, nome));
			
			listarSubHierarchicalCategorias(hierarchicalCategorias, subCategoria, sortDir);
		}
	}

	public Categoria save(Categoria categoria) {
		Categoria pai = categoria.getPai();
		if (pai != null) {
			String todosOsPaisIDs = pai.getTodosOsPaisIDs() == null ? "-" : pai.getTodosOsPaisIDs();
			todosOsPaisIDs += String.valueOf(pai.getId()) + "-";
			categoria.setTodosOsPaisIDs(todosOsPaisIDs);
		}

		return repo.save(categoria);
	}
	
	public List<Categoria> listarCategoriasForm() {
		List<Categoria> listarTodasCategorias = new ArrayList<>();

		Iterable<Categoria> categoriasNoBD = repo.findRootCategorias(Sort.by("nome").ascending());

		for(Categoria categoria : categoriasNoBD) {
			if(categoria.getPai() == null) {
				listarTodasCategorias.add(Categoria.copiarIdENome(categoria));
				
				Set<Categoria> filho = sortSubCategoria(categoria.getFilho());
				
				for(Categoria subCategoria : filho) {
					String nome = "--" + subCategoria.getNome();
					listarTodasCategorias.add(Categoria.copiarIdENome(subCategoria.getId(), nome));

					listarSubCategoriasUsadasNoForm(listarTodasCategorias, subCategoria);
				}
				
			}
		}

		return listarTodasCategorias;
		
	}

	private void listarSubCategoriasUsadasNoForm(List<Categoria> listarTodasCategorias, Categoria pai) {
		Set<Categoria> filho = sortSubCategoria(pai.getFilho());
		
		for(Categoria subCategoria : filho) {
			
			String nome = "----" + subCategoria.getNome();

			listarTodasCategorias.add(Categoria.copiarIdENome(subCategoria.getId(), nome));
			
			listarSubCategoriasUsadasNoForm(listarTodasCategorias, subCategoria);
		}
	}

	public Categoria get(Integer id) throws CategoriaNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoriaNotFoundException("Não foi possível achar nenhuma categoria com o ID " + id);
		}
	}

	public void updateCategoriaStatus(Integer id, boolean habilitado) {
    	repo.updateCategoriaStatus(id, habilitado);
    }	

	public void deletar(Integer id) throws CategoriaNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new CategoriaNotFoundException("Não foi possível achar nenhuma categoria com o ID " + id);
		}

		repo.deleteById(id);
	}

	public String checkUniqueCategoria(Integer id, String nome, String alias) {
		boolean isCreatingNew = (id == null || id == 0);

		Categoria categoriaByName = repo.findByName(nome);

		if (isCreatingNew) {
			if (categoriaByName != null) {
				return "Nome Duplicado";
			} else {
				Categoria categoriaByAlias = repo.findByAlias(alias);
				if (categoriaByAlias != null) {
					return "Alias Duplicado";
				}
			}
		} else {
			if (categoriaByName != null && categoriaByName.getId() != id) {
				return "Nome Duplicado";
			}

			Categoria categoriaByAlias = repo.findByAlias(alias);
			if (categoriaByAlias != null && categoriaByAlias.getId() != id) {
				return "Alias Duplicado";
			}
		}

		return "OK";
	}	

	private SortedSet<Categoria> sortSubCategoria(Set<Categoria> filho) {
		return sortSubCategoria(filho, "asc");
	}

	private SortedSet<Categoria> sortSubCategoria(Set<Categoria> filho, String sortDir) {
		SortedSet<Categoria> sortedFilho = new TreeSet<>(new Comparator<Categoria>() {
		
			@Override
			public int compare(Categoria categ1, Categoria categ2) {
				if (sortDir.equals("asc")) {
					return categ1.getNome().compareTo(categ2.getNome());
				}else {
					return categ2.getNome().compareTo(categ1.getNome());
				}
			}
		});
		
		sortedFilho.addAll(filho);		
		return sortedFilho;
	}

}






