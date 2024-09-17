package com.MotherBoard.Admin.marca;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;
import com.MotherBoard.Admin.FileUploadUtil;
import com.MotherBoard.Admin.categoria.CategoriaServico;
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.Marca;

@Controller
public class MarcaControlador {

	@Autowired
	private MarcaServico servico;
	
	@Autowired
	private CategoriaServico categoriaServico;	
	
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
	
	@PostMapping("/marcas/salvar")
	public String salvaMarca(Marca marca, RedirectAttributes redirectAttributes, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
	    try {
	        Marca saveMarca = servico.save(marca); 

	        if (!multipartFile.isEmpty()) {
	            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	            saveMarca.setLogo(fileName); 

	            String uploadDir = "Marca-logos/" + saveMarca.getId(); 

	            FileUploadUtil.cleanDir(uploadDir); 
	            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); 

	            servico.save(saveMarca); 
	        } else {
	            if (marca.getLogo() == null || marca.getLogo().isEmpty()) {
	                saveMarca.setLogo(null);
	            }
	        }

	        String message = (saveMarca.getId() != null) ? "A marca foi cadastrada com sucesso!" : "Os dados da marca foram atualizados com sucesso!";
	        redirectAttributes.addFlashAttribute("message", message);

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

	
	
	@GetMapping("/marcas/deletar/{id}")
	public String deleteMarca(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
	    try {
	        servico.delete(id);

	        String brandDir = "Marca-logos/" + id;
	        
	        FileUploadUtil.cleanDir(brandDir); 
	        
	        Path dirPath = Paths.get(brandDir);
	        if (Files.exists(dirPath)) {
	            Files.delete(dirPath);
	        }

	        redirectAttributes.addFlashAttribute("message", "A marca com ID " + id + " foi deletada com sucesso!");
	    } 
	    catch (MarcaNotFoundException ex) {
	        redirectAttributes.addFlashAttribute("message", ex.getMessage());
	    } 
	    catch (IOException ex) {
	        redirectAttributes.addFlashAttribute("message", "Erro ao deletar pasta de imagens da marca com ID: " + id);
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



	
}
