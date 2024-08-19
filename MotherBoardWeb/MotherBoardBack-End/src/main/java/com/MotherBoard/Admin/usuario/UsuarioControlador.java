package com.MotherBoard.Admin.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.RedirectAttributesMethodArgumentResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.MotherBoard.entidade.comum.Role;
import com.MotherBoard.entidade.comum.Usuario;

@Controller
public class UsuarioControlador {

	@Autowired
	private UsuarioServico service;
	
	@GetMapping("/Usuarios")
	public String ListaTodosUsuarios(Model model) {
		List<Usuario> listUsuarios = service.listarTodosUsuarios();
		model.addAttribute("listUsuarios", listUsuarios);
		
		
		return "Usuarios";
	}
	
	@GetMapping("/Usuarios/new")
	public String novoUsuario(Model model) {
		List<Role> listRoles = service.listRoles();
		
		Usuario usuario = new Usuario();
		usuario.setPermitido(true);
		
		model.addAttribute("usuario", usuario);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("tituloDaPag", "Cadastrar novo funcionario");
		
		return "Usuario_form";
	}
	
	@PostMapping("/usuarios/salvar")
	public String salvaUsuario(Usuario usuario, RedirectAttributes redirectAttributes) {
	    if (usuario.getId() != null) {
	        redirectAttributes.addFlashAttribute("message", "Os dados do funcionario foram atualizados com sucesso!");
	    } 
	    else {
	        redirectAttributes.addFlashAttribute("message", "O Funcionario foi cadastrado com sucesso!");
	    }
	    service.salva(usuario);
	    return "redirect:/Usuarios";
	}


	
	@GetMapping("/usuarios/editar/{id}")
	public String editaUsuario(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
	    try {
	        Usuario usuario = service.get(id);
	        List<Role> listRoles = service.listRoles();
	        
	        model.addAttribute("usuario", usuario);
	        model.addAttribute("tituloDaPag", "Editar Funcionario (ID: " + id + ")");
	        model.addAttribute("listRoles", listRoles);
	        return "usuario_form";
	    } catch (UserNotFoundException ex) {
	        redirectAttributes.addFlashAttribute("message", ex.getMessage());
	        return "redirect:/Usuarios";  
	    }
	}
	
	@GetMapping("/usuarios/deletar/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
	    try {
	        service.delete(id);
	        redirectAttributes.addFlashAttribute("message", "O Funcionario com ID: " + id + " Foi deletado com sucesso");   
	    }
	    catch (UserNotFoundException ex) {
	        redirectAttributes.addFlashAttribute("message", ex.getMessage());
	    }
	    return "redirect:/Usuarios";
	}

	@GetMapping("/usuarios/{id}/permitido/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean permitido, RedirectAttributes redirectAttributes) {
	    service.updateUserPermStatus(id, permitido);
	    String status = permitido ? "Habilitado" : "Desabilitado";
	    String message = "O ID do funcionario " + id + " foi " + status;
	    redirectAttributes.addFlashAttribute("message", message);
	    return "redirect:/Usuarios";
	}


}