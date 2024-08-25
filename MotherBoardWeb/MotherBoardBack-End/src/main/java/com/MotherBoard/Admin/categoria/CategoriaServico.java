package com.MotherBoard.Admin.categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.Categoria;

@Service
public class CategoriaServico {
	@Autowired
	private CategoriaRepository repo;
	
	public List<Categoria> listAll() {
		return (List<Categoria>) repo.findAll();
	}

	public Categoria save(Categoria categoria) {
		return repo.save(categoria);
	}
	
	public List<Categoria> listarCategoriasForm() {
		List<Categoria> listarTodasCategorias = new ArrayList<>();
		Iterable<Categoria> categoriasNoBD = repo.findAll();

		categoriasNoBD.forEach(categorias -> System.out.println(categorias.getId()));

		for(Categoria categoria : categoriasNoBD) {
			if(categoria.getPai() == null) {
				listarTodasCategorias.add(new Categoria(categoria.getNome()));
				
				Set<Categoria> filho = categoria.getFilho();
				
				for(Categoria subCategoria : filho) {
					String nome = "--" + subCategoria.getNome();
					listarTodasCategorias.add(new Categoria(nome));

					mostrarFilhoSubCategoria(listarTodasCategorias, subCategoria);
				}
			}
		}

		return listarTodasCategorias;
		
	}

	private void mostrarFilhoSubCategoria(List<Categoria> listarTodasCategorias, Categoria pai) {
		Set<Categoria> filho = pai.getFilho();
		
		for(Categoria subCategoria : filho) {
			
			listarTodasCategorias.add(new Categoria("----"+subCategoria.getNome()));
			
			mostrarFilhoSubCategoria(listarTodasCategorias, subCategoria);
		}
	}


}
