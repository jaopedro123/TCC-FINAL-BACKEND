package com.MotherBoard.Admin.categoria;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.MotherBoard.entidade.comum.Categoria;

public interface CategoriaRepository extends PagingAndSortingRepository<Categoria, Integer>, CrudRepository<Categoria, Integer> {
	
    @Query("UPDATE Categoria c SET c.habilitado = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateCategoriaStatus(Integer id, boolean enabled);

}
