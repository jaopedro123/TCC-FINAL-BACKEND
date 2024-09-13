package com.MotherBoard.Admin.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoRestControlador {
 @Autowired private ProdutoServico service;
 
 @PostMapping("/produtos/check_uniquePorduto")
 public String checkuniqueProduto(@Param("id")Integer id, @Param("nome") String nome) {
	 return service.checkUniqueProduto(id, nome);
 }
 
}
