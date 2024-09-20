package com.MotherBoard.Admin.produto;

import com.MotherBoard.entidade.comum.Produto;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProdutoRepositorio extends PagingAndSortingRepository<Produto, Integer> {

	Produto save(Produto produto);

	Iterable<Produto> findAll();

	Optional<Produto> findById(Integer id);

	void deleteById(Integer id);

	public Produto findByNome(String nome);

	@Query("UPDATE Produto p SET p.habilitado = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateHabilitadoStatus(Integer id, boolean habilitado);

	public Long countById(Integer id);

	@Query("SELECT p FROM Produto p WHERE p.nome LIKE %?1%")
	public Page<Produto> pesquisar(String keyword, Pageable pageable);

	@Query("SELECT p FROM Produto p WHERE p.nome LIKE %?1% "
			+ "OR p.descricaoCurta LIKE %?1%"
			+ "OR p.descricaoCompleta LIKE %?1%"
			+ "OR p.marca.nome LIKE %?1%"
			+ "OR p.categoria.nome LIKE %?1%")
	public Page<Produto> findAll(String keyword, Pageable pageable);

	@Query("SELECT p FROM Produto p WHERE p.categoria.id = %?1%" 
			+ "OR p.categoria.todosOsPaisIDs LIKE %?2%")
	public Page<Produto> findAllCategoria(Integer categoriaId, String categoriaIdMatch, Pageable pageable);    

	@Query("SELECT p FROM Produto p WHERE (p.categoria.id = %?1%" 
			+ "OR p.categoria.todosOsPaisIDs LIKE %?2%) AND"
			+ "(p.nome LIKE %?3% "
			+ "OR p.descricaoCurta LIKE %?3%"
			+ "OR p.descricaoCompleta LIKE %?3%"
			+ "OR p.marca.nome LIKE %?3%"
			+ "OR p.categoria.nome LIKE %?3%)")
	public Page<Produto> buscarNaCategoria(Integer categoriaId, String categoriaIdMatch, String keyword, Pageable pageable);

}
