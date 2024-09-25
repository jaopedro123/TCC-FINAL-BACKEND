package com.MotherBoard.Admin.InventarioMarca;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.InventarioCategoria;
import com.MotherBoard.entidade.comum.InventarioMarca;
import com.MotherBoard.entidade.comum.InventarioProduto;

public interface InventarioMarcaRepository extends JpaRepository<InventarioMarca, Integer> {

	@Modifying
	@Query("DELETE FROM InventarioMarca im WHERE im.marca.id = :marcaId")
	void deleteByMarcaId(@Param("marcaId") Integer marcaId);

	

	@Query("SELECT im FROM InventarioMarca im ORDER BY im.dataModificacao DESC")
	List<InventarioMarca> listarTodos();

	@Query("SELECT ip FROM InventarioMarca ip WHERE ip.marca.nome LIKE %?1% "
	        + "OR ip.usuarioId.nomeCompleto LIKE %?1% "
	        + "OR ip.dataModificacao LIKE %?1% "
	        + "ORDER BY ip.dataModificacao DESC")
	Page<InventarioMarca> pesquisar(String keyword, Pageable pageable);



}

