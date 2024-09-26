package com.MotherBoard.Admin.marca;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.MotherBoard.Admin.FileUploadUtil;
import com.MotherBoard.Admin.InventarioMarca.InventarioMarcaService;
import com.MotherBoard.Admin.categoria.CategoriaServico;
import com.MotherBoard.Admin.security.MotherBoarduserDetails;
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.InventarioMarca;
import com.MotherBoard.entidade.comum.Marca;
import com.MotherBoard.entidade.comum.Role;
import com.MotherBoard.entidade.comum.Usuario;

import jakarta.transaction.Transactional;

@Controller
public class MarcaControlador {

	@Autowired
	private MarcaServico servico;
	
	@Autowired
	private CategoriaServico categoriaServico;	
	
	
	@Autowired
	private InventarioMarcaService inventarioMarcaService;
	
	@GetMapping("/marcas")
	public String listAll(Model model) {
	    return listByPage(1, model, "nome", "asc", null);
	}

	
	@GetMapping("/marcas/novo")
	public String newBrand(Model model) {
	    List<Categoria> listCategorias = categoriaServico.listarCategoriasForm();
	    model.addAttribute("listCategorias", listCategorias);
	    model.addAttribute("marca", new Marca());
	    model.addAttribute("tituloDaPag", "Registrar nova Marca");
	    return "/marca_form";
	}
	
	public Usuario getUsuario() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MotherBoarduserDetails) {
	        MotherBoarduserDetails userDetails = (MotherBoarduserDetails) authentication.getPrincipal();
	        return userDetails.getUsuario();  
	    }

	    return null; 
	}
	

	@PostMapping("/marcas/salvar")
	public String salvaMarca(Marca marca, RedirectAttributes redirectAttributes, 
	                         @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

	    try {
	       
	        boolean isNovo = (marca.getId() == null);
	        Marca saveMarca = servico.save(marca);

	       
	        if (!multipartFile.isEmpty()) {
	            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	            saveMarca.setLogo(fileName);
	            String uploadDir = "Marca-logos/" + saveMarca.getId();
	            FileUploadUtil.cleanDir(uploadDir);
	            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	            servico.save(saveMarca); 
	        }

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
     
	        String descricaoInventario = isNovo ? "Adiçao da Marca" : "Atualização da Marca";
	            
		    String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		    
	        InventarioMarca inventario = new InventarioMarca(null, usuario, saveMarca, rolesAsString, dataFormatada, descricaoInventario);
	        inventarioMarcaService.salvaRegistroInventario(inventario);

	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar a marca: " + e.getMessage());
	    }

	    return "redirect:/marcas";
	}

	@GetMapping("/marcas/editar/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) throws MarcaNotFoundException {
	    try {
	        Marca marca = servico.get(id);
	        List<Categoria> listCategorias = categoriaServico.listarCategoriasForm();

	        model.addAttribute("marca", marca);
	        model.addAttribute("listCategorias", listCategorias);
	        model.addAttribute("tituloDaPag", "Editar Marca (ID: " + id + ")");
	        
	       
	        
	        return "/marca_form";
	        
	    } 
	    catch (MarcaNotFoundException ex) {
	        ra.addFlashAttribute("message", ex.getMessage());
	        return "redirect:/marcas";
	    }
	}

	
	

	@Transactional
	@GetMapping("/marcas/deletar/{id}")
	public String deleteMarca(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) throws IOException {
	    try {

	        Usuario usuario = getUsuario();
	        if (usuario == null) {
	            redirectAttributes.addFlashAttribute("errorMessage", "Por favor, faça login para continuar.");
	            return "redirect:/login";
	        }

	        inventarioMarcaService.deleteByMarcaId(id);

	       
	        servico.delete(id);

	        String brandDir = "Marca-logos/" + id;
	        FileUploadUtil.cleanDir(brandDir);
	        
	        
	        

	        redirectAttributes.addFlashAttribute("message", "A marca com ID " + id + " foi deletada com sucesso!");

	    } catch (MarcaNotFoundException ex) {
	        redirectAttributes.addFlashAttribute("message", ex.getMessage());
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar a marca: " + e.getMessage());
	    }

	    return "redirect:/marcas";
	}

	
	@GetMapping("/marcas/page/{pageNum}")
	public String listByPage(
	    @PathVariable(name = "pageNum") int pageNum, 
	    Model model,
	    @RequestParam(name = "sortField", defaultValue = "nome") String sortField, 
	    @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
	    @RequestParam(name = "keyword", required = false) String keyword) {
	    
	    Page<Marca> page = servico.listByPage(pageNum, sortField, sortDir, keyword);
	    List<Marca> listMarcas = page.getContent();
	    
	    long startCount = (pageNum - 1) * MarcaServico.MARCAS_PER_PAGE + 1;
	    long endCount = Math.min(startCount + MarcaServico.MARCAS_PER_PAGE - 1, page.getTotalElements());
	    
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
	    model.addAttribute("listMarcas", listMarcas);
	    
	    return "marcas";
	}


	@GetMapping("/marcas/{id}/habilitado/{status}")
	public String updateMarcaStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean habilitado, RedirectAttributes redirectAttributes) throws MarcaNotFoundException {
	    
	    servico.updateMarcaStatus(id, habilitado);
	    
	    String statusTexto = habilitado ? "Habilitado" : "Desabilitado";
	    String message = "A Marca com ID " + id + " foi " + statusTexto;
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
	    
	    String descricaoInventario = habilitado ? "Marca Ativada" : "Marca Desativada";

	    String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	    
	    InventarioMarca inventario = new InventarioMarca(null, usuario, servico.get(id), rolesAsString, dataFormatada, descricaoInventario);
	    inventarioMarcaService.salvaRegistroInventario(inventario);
	    
	    return "redirect:/marcas";
	}




	
	
}
