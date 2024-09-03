package com.MotherBoard.Admin.usuario;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.MotherBoard.Admin.FileUploadUtil;
import com.MotherBoard.Admin.security.MotherBoarduserDetails;
import com.MotherBoard.entidade.comum.Usuario;

@Controller
public class ContaController {

	
	@Autowired
	private UsuarioServico service;
	
	@GetMapping("/conta")
	public String verDetalhes(@AuthenticationPrincipal MotherBoarduserDetails usuarioLogado, Model model) {
		String email = usuarioLogado.getUsername();
		Usuario usuario = service.getByEmail(email);
		
		model.addAttribute("usuario", usuario);
		
		return "conta_form";
		
	}
	
	@PostMapping("/conta/update")
	public String salvaUsuariodetalhes(Usuario usuario, RedirectAttributes redirectAttributes, @RequestParam("imagem") MultipartFile multipartFile) throws IOException {
	    
	    if (!multipartFile.isEmpty()) {
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        usuario.setFotos(fileName); 
	        Usuario saveUsuario = service.updateConta(usuario); 
	        
	        String uploadDir = "fotos-usuario/" + saveUsuario.getId();
	        	        
	        FileUploadUtil.cleanDir(uploadDir);
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	    } 
	    else {
	    	if (usuario.getFotos().isEmpty()) usuario.setFotos(null);
	        service.updateConta(usuario);
	    }
	    
	        redirectAttributes.addFlashAttribute("message", "Os seus dados foram alterados com sucesso!");
	  
	    
	    return "redirect:/conta";
	}
	
	
}
