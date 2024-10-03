package com.MotherBoard.Admin.InventarioCategoria;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.MotherBoard.entidade.comum.InventarioCategoria;

@Controller
public class InventarioCategoriaControlador {

    @Autowired
    private InventarioCategoriaService categoriaService;

    @GetMapping("/inventarioCategorias")
    public String listarInventarioCategorias(Model model,
    		@RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr) {
    	
    	
        return listByPage(1, model, "dataModificacao", "desc", null, startDateStr, endDateStr);
    }

    @GetMapping("/inventarioCategorias/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum,
            Model model,
            @RequestParam(name = "sortField", defaultValue = "dataModificacao") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate) {
        
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = null;
        }

        DateTimeFormatter formatterISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (startDate != null && !startDate.trim().isEmpty()) {
            try {
                LocalDate startLocalDate = LocalDate.parse(startDate, formatterISO);
                startDate = startLocalDate.format(formatterBR);
            } catch (DateTimeParseException e) {
                System.out.println("Erro ao parsear a data de início: " + e.getMessage());
            }
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            try {
                LocalDate endLocalDate = LocalDate.parse(endDate, formatterISO);
                endDate = endLocalDate.format(formatterBR);
            } catch (DateTimeParseException e) {
                System.out.println("Erro ao parsear a data de fim: " + e.getMessage());
            }
        }
        
        System.out.println("Keyword: " + keyword);
        System.out.println("Start Date (convertido): " + startDate);
        System.out.println("End Date (convertido): " + endDate);
    	
        Page<InventarioCategoria> page = categoriaService.listByPage(pageNum, sortField, sortDir, keyword, startDate, endDate);
        
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
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("listaCategorias", listaCategorias);
        
        return "inventarioCategorias";
    }
}

