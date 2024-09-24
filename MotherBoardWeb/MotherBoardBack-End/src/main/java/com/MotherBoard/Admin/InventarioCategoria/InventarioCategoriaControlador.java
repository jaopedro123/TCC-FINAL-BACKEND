package com.MotherBoard.Admin.InventarioCategoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.MotherBoard.Admin.InventarioMarca.InventarioMarcaService;
import com.MotherBoard.entidade.comum.InventarioCategoria;
import com.MotherBoard.entidade.comum.InventarioMarca;

@Controller
public class InventarioCategoriaControlador {
	
    @Autowired
    private InventarioCategoriaRepository categoriaRepository;
    
    @Autowired
    private InventarioCategoriaService categoriaService;

    @GetMapping("/inventarioCategorias")
    public String listarInventarioCategorias(Model model) {
        return listByPage(1, model, "acao", "asc", null);
    }

    @GetMapping("/inventarioCategorias/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum,
            Model model,
            @RequestParam(name = "sortField", defaultValue = "acao") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false) String keyword) {
        
        Page<InventarioCategoria> page = categoriaService.listByPage(pageNum, sortField, sortDir, keyword);
        List<InventarioCategoria> listaCategorias = page.getContent();
        
        long startCount = (pageNum - 1) * InventarioCategoriaService.INVENTARIO_CATEGORIA_PER_PAGE + 1;
        long endCount = Math.min(startCount + InventarioCategoriaService.INVENTARIO_CATEGORIA_PER_PAGE - 1, page.getTotalElements());
        
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
        model.addAttribute("listaCategorias", listaCategorias);
        
        return "inventarioCategorias";
    }
}

