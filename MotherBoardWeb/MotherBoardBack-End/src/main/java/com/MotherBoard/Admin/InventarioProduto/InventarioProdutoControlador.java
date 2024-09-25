package com.MotherBoard.Admin.InventarioProduto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public String listarInventarioMarcas(Model model) {
        return listByPage(1, model, "dataModificacao", "desc", null);
    }

    @GetMapping("/inventarioProdutos/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum,
            Model model,
            @RequestParam(name = "sortField", defaultValue = "dataModificacao") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(name = "keyword", required = false) String keyword) {

        if ("null".equals(keyword)) {
            keyword = null;
        }
        
        Page<InventarioProduto> page = inventarioProdutoService.listByPage(pageNum, sortField, sortDir, keyword);
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
        model.addAttribute("listaProdutos", listaProdutos);

        return "inventarioProdutos";
    }
}
