package com.MotherBoard.Admin.categoria;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.MotherBoard.Admin.FileUploadUtil;
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.Usuario;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class CategoriaControlador {
	@Autowired
	private CategoriaServico service;
	
	@GetMapping("/categorias")
	public String listAll(Model model) {
		List<Categoria> listarCategorias = service.listAll();
		model.addAttribute("listarCategorias", listarCategorias);
		
		return "Categoria";
	}

	@GetMapping("/categorias/new")
	public String novaCategoria(Model model) {
		List<Categoria>  listarCategorias = service.listarCategoriasForm();

		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listarCategorias", listarCategorias);
		model.addAttribute("tituloDaPag", "Criar nova Categoria");

		return "Categoria_form";
	}
	
/* 	@PostMapping("/categorias/salvar")
	public String nome(Categoria categoria) {
		List<Categoria>  listarCategorias = service.listarCategoriasForm();
		
		System.out.println("Id: "+listarCategorias);
		System.out.println("Nome: "+categoria.getNome());
		System.out.println("Alias: "+categoria.getAlias());
		System.out.println("Ativo: "+categoria.isHabilitado());


		service.save(categoria);
		
		return "redirect:/categorias";
	} */
	

/* 	  @PostMapping("/categorias/salvar") public String salvarCategoria(Categoria
	  categoria,
	  
	  @RequestParam("imagem") MultipartFile multipartFile) throws IOException {
	  String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	  categoria.setImagem(fileName);
	  
	  Categoria savedCategoria = service.save(categoria); 
	  String uploadDir = "../categoria-imagens/" + savedCategoria.getId();
	  FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	  
	  return "redirect:/categorias"; } */
	
	@PostMapping("/categorias/salvar") 
	public String salvarCategoria(Categoria categoria, RedirectAttributes redirectAttributes, @RequestParam("imagem") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			categoria.setImagem(fileName); 
			Categoria savedCategoria = service.save(categoria); 
			
			String uploadDir = "/categoria-imagens/" + savedCategoria.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} 
		else {
			if (categoria.getImagem().isEmpty()) categoria.setImagem(null);
			service.save(categoria);
		}
		
		if (categoria.getId() != null) {
			redirectAttributes.addFlashAttribute("message", "Dados da Categoria atualizado!");
		} 
		else {
			redirectAttributes.addFlashAttribute("message", "A Categoria foi cadastrada com sucesso!");
		}
	
	return "redirect:/categorias"; }
	 
	
}
