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

	@Query("SELECT i FROM InventarioCategoria i WHERE i.categoria.nome LIKE %?1%"
			+ "OR i.usuarioId.nomeCompleto LIKE %?1%"
			+ "OR i.dataModificacao LIKE %?1%")
	Page<InventarioCategoria> pesquisar(String keyword, Pageable pageable);

}
