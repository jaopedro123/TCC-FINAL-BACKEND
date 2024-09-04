package com.MotherBoard.admin.categoria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.aspectj.weaver.ast.CallExpr;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
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
		Categoria categoria = new Categoria("teste6"); 
		Categoria salvaCategoria = repo.save(categoria);
		
		assertThat(salvaCategoria.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testeCreateSubCategoria() {
		Categoria pai = new Categoria(5);
		Categoria processador = new Categoria("Procasdfawerfds", pai);
		Categoria memoriaRam = new Categoria("Memóasdfawerfds", pai);
		Categoria placaDeVideo = new Categoria("Placa dasdfawerfds", pai);
		Categoria armazenamento = new Categoria("Armazeasdfawerfds", pai);
		
//		, memoriaRam, placaDeVideo, armazenamento
		
		repo.saveAll(List.of(processador, memoriaRam, placaDeVideo, armazenamento));
	}
	
	@Test
	public void testeCreateSubDaSubCategoria() {
		Categoria pai = new Categoria(7);
		Categoria subSubCategoria = new Categoria("AMD", pai);
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
	public void testeMostrarCategoriaID() {
		Iterable<Categoria> listarCategorias = repo.findAll();
		listarCategorias.forEach(categorias -> System.out.println(categorias.getId() + " idFilho: " + categorias.getFilho()));
	}
	
	@Test 
	public void testeMostrarSubCategoriaID() {
		Iterable<Categoria> categorias = repo.findAll();
		
		for(Categoria categoria : categorias) {
				
			Set<Categoria> filho = categoria.getFilho();
			
			for(Categoria subCategoria : filho) {
				System.out.println("--"+subCategoria.getId());
			}
		}
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
	
	@Test
	public void testListarRootCategorias() {
		List<Categoria> rootCategorias = repo.findRootCategorias(Sort.by("nome").ascending());

		rootCategorias.forEach(categ -> System.out.println(categ.getNome()));
	}

	@Test 
	public void testFindByName() {
		String nome = "Hardware";
		Categoria categoria = repo.findByName(nome);

		assertThat(categoria).isNotNull();
		assertThat(categoria.getNome()).isEqualTo(nome);
	}
}
