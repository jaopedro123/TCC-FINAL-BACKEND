package com.MotherBoard.Admin.InventarioMarca;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.InventarioMarca;

public interface InventarioMarcaRepository extends JpaRepository<InventarioMarca, Integer> {

	@Modifying
    @Query("DELETE FROM InventarioMarca im WHERE im.marca.id = :marcaId")
    void deleteByMarcaId(@Param("marcaId") Integer marcaId);

	@Query("SELECT im FROM InventarioMarca im WHERE im.acao LIKE %:keyword%")
	Page<InventarioMarca> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);



}

