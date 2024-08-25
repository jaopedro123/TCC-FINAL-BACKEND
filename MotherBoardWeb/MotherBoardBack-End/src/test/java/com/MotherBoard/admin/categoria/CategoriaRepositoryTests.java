package com.MotherBoard.admin.categoria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.MotherBoard.Admin.categoria.CategoriaRepository;
import com.MotherBoard.entidade.comum.Categoria;

@DataJpaTest(showSql = false)
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
		Categoria pai = new Categoria(1);
		Categoria processador = new Categoria("Processador", pai);
		Categoria memoriaRam = new Categoria("Memória Ram", pai);
		Categoria placaDeVideo = new Categoria("Placa de Vídeo", pai);
		Categoria armazenamento = new Categoria("Armazenamento", pai);
		
		repo.saveAll(List.of(processador, memoriaRam, placaDeVideo, armazenamento));
	}
	
	@Test
	public void testeCreateSubDaSubCategoria() {
		Categoria pai = new Categoria(2);
		Categoria subSubCategoria = new Categoria("INTEL", pai);
		Categoria salvaCategoria = repo.save(subSubCategoria);
		
		assertThat(salvaCategoria.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testeGetCategoria() {
		Categoria categoria = repo.findById(1).get();
		System.out.println(categoria.getNome());
		
		Set<Categoria> filho = categoria.getFilho();
		
		for(Categoria subCategoria : filho) {
			System.out.println(subCategoria.getNome());
		}
		
		assertThat(filho.size()).isGreaterThan(0);
	}
	
	@Test
	public void testeMostrarTodasCategoria() {
		Iterable<Categoria> categorias = repo.findAll();
	
		for(Categoria categoria : categorias) {
			if(categoria.getPai() == null) {
				System.out.println(categoria.getNome());
				
				Set<Categoria> filho = categoria.getFilho();
				
				for(Categoria subCategoria : filho) {
					System.out.println("--"+subCategoria.getNome());
					mostrarFilhoSubCategoria(subCategoria);
				}
			}
		}	
	}
	
	private void mostrarFilhoSubCategoria(Categoria pai) {
		Set<Categoria> filho = pai.getFilho();
		
		for(Categoria subCategoria : filho) {
			
			System.out.println("----"+subCategoria.getNome());
			
			mostrarFilhoSubCategoria(subCategoria);
		}
	}
	
}
