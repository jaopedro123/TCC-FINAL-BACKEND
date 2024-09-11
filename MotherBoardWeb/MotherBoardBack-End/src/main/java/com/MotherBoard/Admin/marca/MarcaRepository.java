package com.MotherBoard.Admin.marca;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.Marca;
import com.MotherBoard.entidade.comum.Usuario;

public interface MarcaRepository extends JpaRepository <Marca,Integer> {

	Long countById(Integer id);
	
	Marca findByNome(String nome);
	
	@Query("SELECT m FROM Marca m WHERE m.nome = :nome")
	public Marca getMarcaBynome(@Param("nome") String nome);

    @Query("SELECT m FROM Marca m WHERE m.nome LIKE %:keyword%")
    Page<Marca> findAllBynomeContainingMarca(@Param("keyword") String keyword, Pageable pageable);

	public Page<Marca> findAll(Pageable pageable);
	
	
}
