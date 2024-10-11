package com.MotherBoard.Admin.InventarioProduto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.InventarioProduto;

public interface InventarioProdutoRepository extends JpaRepository<InventarioProduto, Integer> {

    @Modifying
    @Query("DELETE FROM InventarioProduto ip WHERE ip.produto.id = :produtoId")
    void deleteByProdutoId(@Param("produtoId") Integer produtoId);

    
    
    @Query("SELECT ip FROM InventarioProduto ip ORDER BY ip.dataModificacao DESC")
    List<InventarioProduto> listarTodos();

    @Query("SELECT ip FROM InventarioProduto ip WHERE (ip.produto.nome LIKE %?1% OR ip.usuarioId.nomeCompleto LIKE %?1%)"
            + " AND concat(SUBSTRING(ip.dataModificacao, 7, 4), SUBSTRING(ip.dataModificacao, 4, 2), SUBSTRING(ip.dataModificacao, 1, 2)) "
            + "BETWEEN ?2 AND ?3")
    Page<InventarioProduto> pesquisarPorPeriodo(String keyword, String startDateTime, String endDateTime, Pageable pageable);

    @Query("SELECT ip FROM InventarioProduto ip " +
    	       "WHERE concat(SUBSTRING(ip.dataModificacao, 7, 4), SUBSTRING(ip.dataModificacao, 4, 2), SUBSTRING(ip.dataModificacao, 1, 2)) " +
    	       "BETWEEN :startDate AND :endDate")
    	Page<InventarioProduto> pesquisarPorPeriodoSemKeyword(
    	    @Param("startDate") String startDateTime,
    	    @Param("endDate") String endDateTime,
    	    Pageable pageable);


    @Query("SELECT ip FROM InventarioProduto ip WHERE (ip.produto.nome LIKE %?1% OR ip.usuarioId.nomeCompleto LIKE %?1%)")
    Page<InventarioProduto> pesquisar(String keyword, Pageable pageable);

}

