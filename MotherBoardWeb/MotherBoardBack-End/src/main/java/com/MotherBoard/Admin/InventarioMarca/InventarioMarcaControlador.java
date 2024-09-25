package com.MotherBoard.Admin.InventarioMarca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.MotherBoard.entidade.comum.InventarioMarca;

@Controller
public class InventarioMarcaControlador {
    @Autowired
    private InventarioMarcaService inventarioMarcaService;

    @GetMapping("/inventarioMarcas")
    public String listarInventarioMarcas(Model model) {
        List<InventarioMarca> listaMarcas = inventarioMarcaService.listarTodos();
        model.addAttribute("listaMarcas", listaMarcas);
        return listByPage(1, model, "dataModificacao", "desc", null);
    }

    @GetMapping("/inventarioMarcas/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum,
            Model model,
            @RequestParam(name = "sortField", defaultValue = "dataModificacao") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "keyword", required = false) String keyword) {
        
        if ("null".equals(keyword)) {
            keyword = null;
        }
    	
        Page<InventarioMarca> page = inventarioMarcaService.listByPage(pageNum, sortField, sortDir, keyword);
        List<InventarioMarca> listaMarcas = page.getContent();
        
        long startCount = (pageNum - 1) * InventarioMarcaService.INVENTARIO_MARCAS_PER_PAGE + 1;
        long endCount = Math.min(startCount + InventarioMarcaService.INVENTARIO_MARCAS_PER_PAGE - 1, page.getTotalElements());
        
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listaMarcas", listaMarcas);
        
        return "inventarioMarcas";
    }
}

