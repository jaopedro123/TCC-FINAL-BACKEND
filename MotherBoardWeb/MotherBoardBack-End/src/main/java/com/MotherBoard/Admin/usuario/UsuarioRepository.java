package com.MotherBoard.Admin.usuario;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.MotherBoard.entidade.comum.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer>{
	
	@Query("SELECT u FROM Usuario u WHERE u.email = :email")
	public Usuario getUserByEmail(@Param("email") String email);

	@Query("SELECT u FROM Usuario u WHERE u.cpf = :cpf")
	public Usuario getUserByCpf(@Param("cpf") String cpf);


	public Long countById(Integer id);
	
	@Query("UPDATE Usuario u SET u.permitido = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	
}
