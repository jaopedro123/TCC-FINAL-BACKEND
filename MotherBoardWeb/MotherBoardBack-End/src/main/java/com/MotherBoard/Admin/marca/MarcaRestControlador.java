package com.MotherBoard.Admin.marca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarcaRestControlador {

	@Autowired
	private MarcaServico servico;
	
	@PostMapping("/marcas/check_unique")
	public String checkUniqueMarca(@Param("id") Integer id, @Param("nome") String nome) {
	    return servico.checkUniqueMarca(id, nome) ? "OK" : "Duplicate";
	}


}
