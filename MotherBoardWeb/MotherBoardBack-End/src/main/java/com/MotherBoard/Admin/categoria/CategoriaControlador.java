package com.MotherBoard.Admin.categoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.MotherBoard.entidade.comum.Categoria;

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
}
