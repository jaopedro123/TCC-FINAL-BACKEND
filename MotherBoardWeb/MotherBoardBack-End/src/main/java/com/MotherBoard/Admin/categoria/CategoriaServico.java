package com.MotherBoard.Admin.categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.Categoria;

import jakarta.transaction.Transactional;

@Service
@Transactional
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

		for(Categoria categoria : categoriasNoBD) {
			if(categoria.getPai() == null) {

				listarTodasCategorias.add(Categoria.copiarIdENome(categoria));
				
				Set<Categoria> filho = categoria.getFilho();
				
				for(Categoria subCategoria : filho) {
					String nome = "---- " + "Id: " + subCategoria.getId() + ", subCategoria: " + subCategoria.getNome();
					listarTodasCategorias.add(Categoria.copiarIdENome(subCategoria.getId(), nome));

					mostrarFilhoSubCategoria(listarTodasCategorias, subCategoria);
				}
				
			}
		}

		return listarTodasCategorias;
		
	}

	private void mostrarFilhoSubCategoria(List<Categoria> listarTodasCategorias, Categoria pai) {
		Set<Categoria> filho = pai.getFilho();
		
		for(Categoria subCategoria : filho) {
			
			String nome = "----" + subCategoria.getNome();

			listarTodasCategorias.add(Categoria.copiarIdENome(subCategoria.getId(), nome));
			
			mostrarFilhoSubCategoria(listarTodasCategorias, subCategoria);
		}
	}

	public void updateCategoriaStatus(Integer id, boolean habilitado) {
    	repo.updateCategoriaStatus(id, habilitado);
    }

}
