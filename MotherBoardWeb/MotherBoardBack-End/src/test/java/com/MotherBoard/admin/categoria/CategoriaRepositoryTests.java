package com.MotherBoard.admin.categoria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.MotherBoard.Admin.categoria.CategoriaRepository;
import com.MotherBoard.entidade.comum.Categoria;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoriaRepositoryTests {

	@Autowired
	private CategoriaRepository repo;
	
	@Test
	public void testeCreateRootCategoria() {
		Categoria categoria = new Categoria("Hardware");
		Categoria salvaCategoria = repo.save(categoria);
		
		assertThat(salvaCategoria.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testeCreateSubCategoria() {
		Categoria parent = new Categoria(1);
		Categoria subCategoria = new Categoria("Memória Ram", parent);
		
		Categoria salvaCategoria = repo.save(subCategoria);
		
		assertThat(salvaCategoria.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testeGetCategoria() {
		Categoria categoria = repo.findById(1).get();
		System.out.println(categoria.getNome());
		
		Set<Categoria> children = categoria.getChildren();
		
		for(Categoria subCategoria : children) {
			System.out.println(subCategoria.getNome());
		}
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
}
