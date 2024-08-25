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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

	@PostMapping("/categorias/salvar")
	public String salvarCategoria(Categoria categoria, RedirectAttributes redirectAttributes,
		@RequestParam("imagem") MultipartFile multipartFile) throws IOException {
//			String arquivoNome = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			categoria.setImagem(arquivoNome);
//
//			Categoria salvarCategoria = service.save(categoria);
//			String enviarDir = "../categoria-imagComputadoresens" + salvarCategoria.getId();
//			FileUploadUtil.saveFile(enviarDir, arquivoNome, multipartFile);
			
			
			if (!multipartFile.isEmpty()) {
		        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		        categoria.setImagem(fileName); 
		        Categoria savecategoria = service.save(categoria); 
		        
		        String uploadDir = "categoria-imagens/" + savecategoria.getId();
		        
		        FileUploadUtil.cleanDir(uploadDir);
		        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		    } 
		    else {
		    	if (categoria.getImagem().isEmpty()) categoria.setImagem(null);
		        service.save(categoria);
		    }
		    
		    if (categoria.getId() != null) {
		        redirectAttributes.addFlashAttribute("message", "Os dados do funcionário foram atualizados com sucesso!");
		    } 
		    else {
		        redirectAttributes.addFlashAttribute("message", "O funcionário foi cadastrado com sucesso!");
		    }

		return "redirect:/categorias";
	}
	
}
