package com.MotherBoard.Admin.usuario;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.MotherBoard.entidade.comum.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{
	
	

	}
