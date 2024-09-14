package com.MotherBoard.Admin.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.MotherBoard.Admin.marca.MarcaServico;
import com.MotherBoard.entidade.comum.Marca;
import com.MotherBoard.entidade.comum.Produto;

@Controller
public class ProdutoControlador {
@Autowired private ProdutoServico  produtoServico;
@Autowired private MarcaServico marcaServico;
 @GetMapping("/produtos")
 public String listAll(Model model) {
	 List<Produto> listaProdutos = produtoServico.listAll();
	 model.addAttribute("listaProdutos", listaProdutos);
	return "Produto";
	 
 }
 @GetMapping("/produtos/new")
 public String newProduto(Model model) {
	 List<Marca> listMarcas = marcaServico.listAll();
	 
	 Produto produto = new Produto();
	 produto.setEnabled(true);
	 produto.setInStock(true);
	 
	 model.addAttribute("produto", produto);
	 model.addAttribute("listMarcas", listMarcas);
	 model.addAttribute("tituloDaPag", "Criar novo produto");
	 return "produto_form";
 }
 @PostMapping("/produtos/salvar")
 public String saveProduto(Produto produto, RedirectAttributes ra) {
	 produtoServico.save(produto);
	ra.addFlashAttribute("mensagem","o produto foi salvo com sucesso");
	return "redirect:/produtos";
 }
 
 @GetMapping("/produtos/{id}/habilitado/{status}")
 public String updateCategoriaStatus(@PathVariable("id") Integer id,
		 @PathVariable("status") boolean habilitado, RedirectAttributes redirectAttributes) {
	 produtoServico.updatePordutoHabilitadoStatus(id, habilitado);
	 String status = habilitado ? "habilitado" : "desabilitado";
	 String mensagem = "o produto de id " + id + " foi " + status;
	 redirectAttributes.addFlashAttribute("mensagem", mensagem);
	 return "redirect:/produtos";
 };
}
