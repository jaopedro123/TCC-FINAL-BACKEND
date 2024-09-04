package com.MotherBoard.Admin.categoria;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.Categoria;

public interface CategoriaRepository extends PagingAndSortingRepository<Categoria, Integer>, CrudRepository<Categoria, Integer> {
	
	@Query("SELECT c FROM Categoria c WHERE c.pai.id is NULL")
	public List<Categoria> findRootCategorias(Sort sort);

	@Query("SELECT c FROM Categoria c WHERE c.pai.id is NULL")
	public Page<Categoria> findRootCategorias(Pageable pageable);

	@Query("SELECT c FROM Categoria c WHERE c.nome LIKE %?1%")
	public Page<Categoria> pesquisar(String keyword, Pageable pageable);

	public Long countById(Integer id);
	
	@Query("SELECT c FROM Categoria c WHERE c.nome = :nome")
	public Categoria findByName(@Param("nome") String nome);
	
	@Query("SELECT c FROM Categoria c WHERE c.alias = :alias")
	public Categoria findByAlias(@Param("alias") String alias);

    @Query("UPDATE Categoria c SET c.habilitado = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateCategoriaStatus(Integer id, boolean enabled);

}
