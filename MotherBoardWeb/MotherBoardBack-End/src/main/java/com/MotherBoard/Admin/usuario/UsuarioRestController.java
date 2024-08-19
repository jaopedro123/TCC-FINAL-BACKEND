package com.MotherBoard.Admin.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioRestController {
	
	@Autowired
	private UsuarioServico service;

	@PostMapping("/Usuarios/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
	    return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}

	@PostMapping("/Usuarios/check_cpf")
	public String checkDuplicateCpf(@Param("id") Integer id, @Param("cpf") String cpf) {
	    return service.isCpfUnique(id, cpf) ? "OK" : "Duplicated";
	}
	
}
