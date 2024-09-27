package com.MotherBoard.Admin.InventarioProduto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.MotherBoard.entidade.comum.InventarioProduto;

@Controller
public class InventarioProdutoControlador {

    @Autowired
    private InventarioProdutoService inventarioProdutoService;


    @GetMapping("/inventarioProdutos")
    public String listarInventarioMarcas(Model model,
                                         @RequestParam(value = "startDate", required = false) String startDateStr,
                                         @RequestParam(value = "endDate", required = false) String endDateStr) {
        return listByPage(1, model, "dataModificacao", "desc", null, startDateStr, endDateStr);
    }

    @GetMapping("/inventarioProdutos/page/{pageNum}")
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
        
        if (startDate == null || startDate.trim().isEmpty()) {
        	startDate = null;
        }

        if (endDate == null || endDate.trim().isEmpty()) {
        	endDate = null;
        }
        
        System.out.println("Keyword: " + keyword);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        
        
        Page<InventarioProduto> page = inventarioProdutoService.listByPage(pageNum, sortField, sortDir, keyword, startDate, endDate);
        List<InventarioProduto> listaProdutos = page.getContent();

        long startCount = (pageNum - 1) * InventarioProdutoService.INVENTARIO_PRODUTOS_PER_PAGE + 1;
        long endCount = Math.min(startCount + InventarioProdutoService.INVENTARIO_PRODUTOS_PER_PAGE - 1, page.getTotalElements());

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
        model.addAttribute("listaProdutos", listaProdutos);
        
        System.out.println("Lista Produtos: " + listaProdutos);
        
        return "inventarioProdutos";
        
        
    }



}
