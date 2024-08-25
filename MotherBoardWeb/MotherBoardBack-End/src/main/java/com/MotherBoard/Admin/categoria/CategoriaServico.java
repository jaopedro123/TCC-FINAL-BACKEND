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

		/* categoriasNoBD.forEach(categorias -> System.out.println(categorias.getId())); */

		for(Categoria categoria : categoriasNoBD) {
			if(categoria.getPai() == null) {

				/* String teste = "Id: " + categoria.getId() + ", categoria: " + categoria.getNome(); */
				listarTodasCategorias.add(Categoria.copiarIdENome(categoria));
				
				/* System.out.println("BBBBBBBBBBBBBBBBBBBB"+Categoria.copiarIdENome(categoria)); */
				
				Set<Categoria> filho = categoria.getFilho();
				
				for(Categoria subCategoria : filho) {
					String nome = "---- " + "Id: " + subCategoria.getId() + ", subCategoria: " + subCategoria.getNome();
					listarTodasCategorias.add(Categoria.copiarIdENome(subCategoria.getId(), nome));

					/* System.out.println("AAAAAAAAAAAAAAAAAAA"+Categoria.copiarIdENome(subCategoria.getId(), nome)); */
					
					mostrarFilhoSubCategoria(listarTodasCategorias, subCategoria);
				}
				
			}
		}

		/* System.out.println("TESTEEEEEEEEE: "+listarTodasCategorias); */

		return listarTodasCategorias;
		
	}

	private void mostrarFilhoSubCategoria(List<Categoria> listarTodasCategorias, Categoria pai) {
		Set<Categoria> filho = pai.getFilho();
		
		for(Categoria subCategoria : filho) {
			
			String nome = "----" + subCategoria.getNome();

			listarTodasCategorias.add(Categoria.copiarIdENome(subCategoria.getId(), nome));
			
			/* System.out.println("GGGGGGGGGGGGGGGGG2"+nome); */
			
			mostrarFilhoSubCategoria(listarTodasCategorias, subCategoria);
		}
	}


}
