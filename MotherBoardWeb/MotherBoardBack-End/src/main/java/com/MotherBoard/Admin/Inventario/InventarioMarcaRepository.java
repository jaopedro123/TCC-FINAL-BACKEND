package com.MotherBoard.Admin.Inventario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.InventarioMarca;

public interface InventarioMarcaRepository extends JpaRepository<InventarioMarca, Integer> {
	
	
	
	@Modifying
    @Query("DELETE FROM InventarioMarca im WHERE im.marca.id = :marcaId")
    void deleteByMarcaId(@Param("marcaId") Integer marcaId);
}

