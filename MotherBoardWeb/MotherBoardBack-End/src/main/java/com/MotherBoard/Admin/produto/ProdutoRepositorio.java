package com.MotherBoard.Admin.produto;

import com.MotherBoard.entidade.comum.Produto;

import java.util.Optional;

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

}
