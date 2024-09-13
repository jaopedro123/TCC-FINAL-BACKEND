package com.MotherBoard.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.data.repository.CrudRepository;

import com.MotherBoard.Admin.marca.MarcaRepository;
import com.MotherBoard.Admin.produto.ProdutoRepositorio;
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.Marca;
import com.MotherBoard.entidade.comum.Produto;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProdutoRepositorio repo;
	
	@Autowired
	private MarcaRepository repO;
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testcreateProduct() {
	  Categoria categoria = entityManager.find(Categoria.class, 4);
	  Marca marca = entityManager.find(Marca.class, 1);
	  Produto produto = new Produto();
	  produto.setNome("so com marca");
	  produto.setAlias("teste_marcado");
	  produto.setDescricaoCurta("uma bom marca");
	  produto.setDescricaoCompleta("e um bom marca com a desc completa");
	  
	  produto.setCategoria(categoria);
	  produto.setMarca(marca);
	  
	  produto.setPreco(333);
	  produto.setCusto(662);
	  produto.setHabilitado(true);
	  produto.setNoStoque(true);
	  produto.setTempoDaCriacao(new Date());
	  produto.setTempoAtualizado(new Date());
	  
	  Produto savedProduto = repo.save(produto);
	  
	  assertThat(savedProduto).isNotNull();
	  assertThat(savedProduto.getId()).isGreaterThan(0);
	}
	@Test
	public void testListAllProducts() {
		Iterable<Produto> iterableProdutos = repo.findAll();
		
		iterableProdutos.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 1;
	  Optional<Produto> produto = repo.findById(id);
	    System.out.println(produto);
	    assertThat(produto).isNotNull();
	}
	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Produto produto = repo.findById(id).get();
		produto.setPreco(555);
		
		repo.save(produto);
		Produto updateProduto = entityManager.find(Produto.class, id);
		
		assertThat(updateProduto.getPreco()).isEqualTo(555);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id = 12;
		repo.deleteById(id);
		
		Optional<Produto> result = repo.findById(id);
		assertThat(!result.isPresent());
	}

}
