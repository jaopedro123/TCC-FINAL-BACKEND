package com.MotherBoard.Admin.InventarioProduto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.MotherBoard.Admin.export.InventarioProdExcelExporter;
import com.MotherBoard.Admin.export.InventarioProdPdfExporter;
import com.MotherBoard.entidade.comum.InventarioProduto;

import jakarta.servlet.http.HttpServletResponse;

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

        Page<InventarioProduto> page = inventarioProdutoService.listByPage(pageNum, sortField, sortDir, keyword, startDate, endDate);
        

        if (keyword != null && startDate != null && endDate != null) {
            page = inventarioProdutoService.listByPage(pageNum, sortField, sortDir, keyword, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            page = inventarioProdutoService.listByPage(pageNum, sortField, sortDir, null, startDate, endDate);
        } else if (keyword != null) {
            page = inventarioProdutoService.listByPage(pageNum, sortField, sortDir, keyword, null, null);
        } else {
            page = inventarioProdutoService.listByPage(pageNum, sortField, sortDir, null, null, null);
        }

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

        System.out.println("listaProdutos " + listaProdutos);
        return "inventarioProdutos";
    }
    
    
	@GetMapping("/inventarioProdutos/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
	    List<InventarioProduto> listInventarioProduto = inventarioProdutoService.listAll();
	    
	    if (listInventarioProduto == null) {
	    	listInventarioProduto = new ArrayList<>();
	     
	    }

	    InventarioProdExcelExporter exporter = new InventarioProdExcelExporter();
	    exporter.export(listInventarioProduto, response);
	}
	
	@GetMapping("/inventarioProdutos/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
	    List<InventarioProduto> listInventarioProduto = inventarioProdutoService.listAll();
	    
	    if (listInventarioProduto == null) {
	    	listInventarioProduto = new ArrayList<>();
	     
	    }

	    InventarioProdPdfExporter exporter = new InventarioProdPdfExporter();
	    exporter.export(listInventarioProduto, response);
	}


}
