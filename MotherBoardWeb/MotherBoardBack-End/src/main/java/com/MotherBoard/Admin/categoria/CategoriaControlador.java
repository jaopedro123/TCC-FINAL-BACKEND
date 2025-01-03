package com.MotherBoard.Admin.categoria;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.MotherBoard.Admin.FileUploadUtil;
import com.MotherBoard.Admin.InventarioCategoria.InventarioCategoriaService;
import com.MotherBoard.Admin.security.MotherBoarduserDetails;
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.InventarioCategoria;
import com.MotherBoard.entidade.comum.Role;
import com.MotherBoard.entidade.comum.Usuario;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoriaControlador {
	@Autowired
	private CategoriaServico service;
	
	@Autowired
	private InventarioCategoriaService categoriaService;
	
	
	
	
	public Usuario getUsuario() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MotherBoarduserDetails) {
	        MotherBoarduserDetails userDetails = (MotherBoarduserDetails) authentication.getPrincipal();
	        return userDetails.getUsuario();  
	    }

	    return null; 
	}
	
	@GetMapping("/categorias")
	public String listFirtPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}

	@GetMapping("/categorias/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, @Param("sortDir") String sortDir,@Param("keyword") String keyword, Model model) {

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "desc";
		}

		CategoriaPaginaInfo pageInfo = new CategoriaPaginaInfo();

		List<Categoria> listarCategorias = service.listByPage(pageInfo, pageNum, sortDir, keyword);
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("totalPages", pageInfo.getTotalPaginas());
		model.addAttribute("totalItems", pageInfo.getTotalElementos());
		model.addAttribute("paginaAtual", pageNum);
		model.addAttribute("keyword", keyword);

		model.addAttribute("listarCategorias", listarCategorias);
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "Categoria";
	}	
	

	@GetMapping("/categorias/new")
	public String novaCategoria(Model model) {
		List<Categoria> listarCategorias = service.listarCategoriasForm();

		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listarCategorias", listarCategorias);
		model.addAttribute("tituloDaPag", "Criar nova Categoria");

		return "Categoria_form";
	}
	
	@PostMapping("/categorias/salvar") 
	public String salvarCategoria(Categoria categoria, RedirectAttributes redirectAttributes, @RequestParam("img") MultipartFile multipartFile) throws IOException {

	    try {
		       
	        boolean isNovo = (categoria.getId() == null);
	        Categoria savecategoria = service.save(categoria);
		
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			categoria.setImagem(fileName); 

			Categoria savedCategoria = service.save(categoria); 
			String uploadDir = "categoria-imagens/" + savedCategoria.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
		

        
		else {
			if (categoria.getImagem().isEmpty()) categoria.setImagem(null);
			service.save(categoria);
		}
		
        Usuario usuario = getUsuario();
        Set<Role> roles = usuario.getRoles();
        String rolesAsString = roles.stream()
                                    .map(Role::getNome)  
                                    .reduce((role1, role2) -> role1 + ", " + role2)
                                    .orElse("Sem Papel");
 
        String descricaoInventario = isNovo ? "Adiçao da Categoria" : "Atualização da Categoria";
            
	    String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	    
        InventarioCategoria inventario = new InventarioCategoria(null, usuario, savecategoria, rolesAsString, descricaoInventario , dataFormatada);
        categoriaService.salvaRegistroInventario(inventario);
		


		if (categoria.getId() != null) {
	        redirectAttributes.addFlashAttribute("message", "Os dados da categoria foram atualizados com sucesso!");
	    } 
	    else {
	        redirectAttributes.addFlashAttribute("message", "A categoria foi cadastrada com sucesso!");
	    }
		
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar a Categoria: " + e.getMessage());
	    }

		return "redirect:/categorias"; 
	}

	@GetMapping("/categorias/editar/{id}")
	public String editarCategoria(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Categoria categoria = service.get(id);
			List<Categoria> listarCategorias = service.listarCategoriasForm();

			model.addAttribute("categoria", categoria);
			model.addAttribute("listarCategorias", listarCategorias);
			model.addAttribute("tituloDaPag", "Editar Categoria (ID:  "+id+ ")");

			return "categoria_form";

		} catch (CategoriaNotFoundException ex) {
			ra.addFlashAttribute("message",ex.getMessage());
			return "redirect:/categorias";
		}
	}
	

	@GetMapping("/categorias/{id}/habilitado/{status}")
	public String updateCategoriaStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean habilitado, RedirectAttributes redirectAttributes) throws CategoriaNotFoundException {

	    service.updateCategoriaStatus(id, habilitado);
	    String status = habilitado ? "Habilitado" : "Desabilitado";
	    String message = "A Categoria com " + id + " foi " + status;
	    redirectAttributes.addFlashAttribute("message", message);
	    
	    Usuario usuario = getUsuario();
	    
	    if (usuario == null) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Por favor, faça login para continuar.");
	        return "redirect:/login";
	    }
	    
	    Set<Role> roles = usuario.getRoles();
	    String rolesAsString = roles.stream()
	                                .map(Role::getNome)
	                                .reduce((role1, role2) -> role1 + ", " + role2)
	                                .orElse("Sem Papel");
	                                
	    String descricaoInventario = habilitado ? "Categoria Ativada" : "Categoria Desativada";

	    String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	    
        InventarioCategoria inventario = new InventarioCategoria(null, usuario, service.get(id), rolesAsString, descricaoInventario , dataFormatada);
        categoriaService.salvaRegistroInventario(inventario);
	    
	    
		return "redirect:/categorias";
	}

	@GetMapping("/categorias/deletar/{id}")
	public String deletarCategoria(@PathVariable(name = "id") Integer id, RedirectAttributes ra) throws IOException {
		try {
			service.deletar(id);
			String categoryDir = "categoria-imagens/" + id;
			FileUploadUtil.cleanDir(categoryDir); 
	        FileUploadUtil.deleteDir(categoryDir);

			ra.addFlashAttribute("message", "A categoria ID " + id + " foi deletada com sucesso");
		} catch (CategoriaNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/categorias";
	}
	
}
