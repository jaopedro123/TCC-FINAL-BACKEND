package com.MotherBoard.Admin.categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class CategoriaRestController {

    @Autowired
    private CategoriaServico servico;

    @PostMapping("/categorias/check_unique")
    public String checkUniqueCategoria(@Param("id") Integer id, @Param("nome") String nome, 
        @Param("alias") String alias) {
        
        return servico.checkUniqueCategoria(id, nome, alias);
    }
}
