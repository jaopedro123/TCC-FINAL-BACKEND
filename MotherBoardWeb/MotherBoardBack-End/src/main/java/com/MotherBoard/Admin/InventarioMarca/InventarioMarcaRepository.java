package com.MotherBoard.Admin.InventarioMarca;

import java.util.List;

import org.springframework.data.domain.Page;
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

	

	@Query("SELECT im FROM InventarioMarca im ORDER BY im.dataModificacao DESC")
	List<InventarioMarca> listarTodos();

	@Query("SELECT im FROM InventarioMarca im WHERE (im.marca.nome LIKE %?1% OR im.usuarioId.nomeCompleto LIKE %?1%)"
            + " AND concat(SUBSTRING(im.dataModificacao, 7, 4), SUBSTRING(im.dataModificacao, 4, 2), SUBSTRING(im.dataModificacao, 1, 2)) "
            + "BETWEEN ?2 AND ?3")
    Page<InventarioMarca> pesquisarPorPeriodo(String keyword, String startDateTime, String endDateTime, Pageable pageable);

    @Query("SELECT im FROM InventarioMarca im " +
           "WHERE concat(SUBSTRING(im.dataModificacao, 7, 4), SUBSTRING(im.dataModificacao, 4, 2), SUBSTRING(im.dataModificacao, 1, 2)) " +
           "BETWEEN :startDate AND :endDate")
    Page<InventarioMarca> pesquisarPorPeriodoSemKeyword(
        @Param("startDate") String startDateTime,
        @Param("endDate") String endDateTime,
        Pageable pageable);

    @Query("SELECT im FROM InventarioMarca im WHERE (im.marca.nome LIKE %?1% OR im.usuarioId.nomeCompleto LIKE %?1%)")
    Page<InventarioMarca> pesquisar(String keyword, Pageable pageable);



}

