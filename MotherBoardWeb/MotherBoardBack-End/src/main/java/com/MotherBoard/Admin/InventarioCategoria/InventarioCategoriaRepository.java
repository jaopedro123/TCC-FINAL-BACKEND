package com.MotherBoard.Admin.InventarioCategoria;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.InventarioCategoria;

public interface InventarioCategoriaRepository extends JpaRepository<InventarioCategoria, Integer> {

	@Modifying
    @Query("DELETE FROM InventarioCategoria i WHERE i.categoria.id = :categoria")
    void deleteByCategoriaId(@Param("categoria") Integer categoriaId);

	@Query("SELECT i FROM InventarioCategoria i ORDER BY i.dataModificacao DESC")
	List<InventarioCategoria> listarTodos();

	@Query("SELECT i FROM InventarioCategoria i WHERE (i.categoria.nome LIKE %?1% OR i.usuarioId.nomeCompleto LIKE %?1%)"
            + " AND concat(SUBSTRING(i.dataModificacao, 7, 4), SUBSTRING(i.dataModificacao, 4, 2), SUBSTRING(i.dataModificacao, 1, 2)) "
            + "BETWEEN ?2 AND ?3")
    Page<InventarioCategoria> pesquisarPorPeriodo(String keyword, String startDateTime, String endDateTime, Pageable pageable);

    @Query("SELECT i FROM InventarioCategoria i " +
           "WHERE concat(SUBSTRING(i.dataModificacao, 7, 4), SUBSTRING(i.dataModificacao, 4, 2), SUBSTRING(i.dataModificacao, 1, 2)) " +
           "BETWEEN :startDate AND :endDate")
    Page<InventarioCategoria> pesquisarPorPeriodoSemKeyword(
        @Param("startDate") String startDateTime,
        @Param("endDate") String endDateTime,
        Pageable pageable);

    @Query("SELECT i FROM InventarioCategoria i WHERE (i.categoria.nome LIKE %?1% OR i.usuarioId.nomeCompleto LIKE %?1%)")
    Page<InventarioCategoria> pesquisar(String keyword, Pageable pageable);

}
