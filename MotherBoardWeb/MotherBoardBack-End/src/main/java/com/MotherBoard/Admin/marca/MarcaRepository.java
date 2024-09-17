package com.MotherBoard.Admin.marca;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.Marca;

public interface MarcaRepository extends JpaRepository <Marca,Integer> {

	Long countById(Integer id);
	
	Marca findByNome(String nome);
	
	@Query("SELECT m FROM Marca m WHERE m.nome = :nome")
	public Marca getMarcaBynome(@Param("nome") String nome);

    @Query("SELECT m FROM Marca m WHERE m.nome LIKE %:keyword%")
    Page<Marca> findAllBynomeContainingMarca(@Param("keyword") String keyword, Pageable pageable);

	public Page<Marca> findAll(Pageable pageable);
	
	@Query("SELECT NEW Marca(m.id, m.nome) FROM Marca m ORDER BY m.nome ASC")
	public List<Marca> findAll();
}
