package com.MotherBoard.Admin.Inventario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.MotherBoard.entidade.comum.InventarioMarca;

@Controller
public class InventarioMarcaControlador {
    @Autowired
    private InventarioMarcaService inventarioMarcaService;

    @GetMapping("/inventarioMarcas")
    public String listarInventarioMarcas(Model model) {
        List<InventarioMarca> listaMarcas = inventarioMarcaService.listarTodos();
        model.addAttribute("listaMarcas", listaMarcas);
        return "inventarioMarcas"; 
    }
    
}

