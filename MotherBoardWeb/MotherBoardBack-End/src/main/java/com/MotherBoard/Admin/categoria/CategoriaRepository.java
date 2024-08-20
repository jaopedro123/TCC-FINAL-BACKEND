package com.MotherBoard.Admin.categoria;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.MotherBoard.entidade.comum.Categoria;

public interface CategoriaRepository extends PagingAndSortingRepository<Categoria, Integer> {
	
}
