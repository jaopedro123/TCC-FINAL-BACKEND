package com.MotherBoard.Admin.produto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import com.MotherBoard.Admin.InventarioProduto.InventarioProdutoService;
import com.MotherBoard.Admin.categoria.CategoriaServico;
import com.MotherBoard.Admin.marca.MarcaServico;
import com.MotherBoard.Admin.security.MotherBoarduserDetails;
import com.MotherBoard.entidade.comum.Categoria;
import com.MotherBoard.entidade.comum.InventarioProduto;
import com.MotherBoard.entidade.comum.Marca;
import com.MotherBoard.entidade.comum.Produto;
import com.MotherBoard.entidade.comum.ProdutoImagem;
import com.MotherBoard.entidade.comum.Role;
import com.MotherBoard.entidade.comum.Usuario;

@Controller
public class ProdutoControlador {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoControlador.class);

	@Autowired
	private ProdutoServico produtoServico;
	@Autowired
	private MarcaServico marcaServico;
	@Autowired
	private CategoriaServico categoriaServico;
	
	@Autowired
	private InventarioProdutoService inventarioProdutoService;

	@GetMapping("/produtos")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "id", "asc", null, 0);
	}
	
	public Usuario getUsuario() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MotherBoarduserDetails) {
	        MotherBoarduserDetails userDetails = (MotherBoarduserDetails) authentication.getPrincipal();
	        return userDetails.getUsuario();  
	    }

	    return null; 
	}

	@GetMapping("/produtos/page/{pageNum}")
	public String listByPage(
	    @PathVariable(name = "pageNum") int pageNum, Model model,
	    @Param("sortField") String sortField, 
	    @Param("sortDir") String sortDir,
	    @Param("keyword") String keyword,
	    @Param("categoriaId") Integer categoriaId) {

	    Page<Produto> page = produtoServico.listByPage(pageNum, sortField, sortDir, keyword, categoriaId);
	    List<Produto> listProdutos = page.getContent();
	    
		List<Categoria> listCategorias = categoriaServico.listarCategoriasForm();

	    long startCount = (pageNum - 1) * ProdutoServico.PRODUTOS_POR_PAGE + 1;
	    long endCount = Math.min(startCount + ProdutoServico.PRODUTOS_POR_PAGE - 1, page.getTotalElements());
	    
	    String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		if (categoriaId != null) model.addAttribute("categoriaId", categoriaId);
	    
	    model.addAttribute("currentPage", pageNum);
	    model.addAttribute("totalPages", page.getTotalPages());
	    model.addAttribute("startCount", startCount);
	    model.addAttribute("endCount", endCount);
	    model.addAttribute("totalItems", page.getTotalElements());
	    model.addAttribute("sortField", sortField);
	    model.addAttribute("sortDir", sortDir);
	    model.addAttribute("reverseSortDir", reverseSortDir);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("listProdutos", listProdutos);
	    model.addAttribute("listCategorias", listCategorias);
	    
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
		model.addAttribute("numeroDeImagensExtras", 0);
		
		return "produto_form";
	}

	@PostMapping("/produtos/salvar")
	public String saveProduto(Produto produto, RedirectAttributes ra,
	        @RequestParam("fileImage") MultipartFile imagemPrincipalMultipart,
	        @RequestParam("imagemExtra") MultipartFile[] imagemExtraMultiparts,
	        @RequestParam(name = "detalhesIDs", required = false) String[] detalhesIDs,
	        @RequestParam(name = "detalhesNomes", required = false) String[] detalhesNomes,
	        @RequestParam(name = "detalhesValor", required = false) String[] detalhesValor,
	        @RequestParam(name = "imagemIDs", required = false) String[] imagemIDs,
	        @RequestParam(name = "imagemNomes", required = false) String[] imagemNomes)
	        throws IOException, ProdutoNotFoundException {

	    setImagemPrincipalNome(imagemPrincipalMultipart, produto);
	    setImagensExtraNomesExistentes(imagemIDs, imagemNomes, produto);
	    setNewImagemExtraNome(imagemExtraMultiparts, produto);
	    setProdutoDetalhes(detalhesIDs, detalhesNomes, detalhesValor, produto);

	    boolean isNovo = (produto.getId() == null);

	    // Captura o estoque anterior antes de salvar o produto
	    String quantidadeEstoqueAnterior = isNovo ? "0" : produtoServico.get(produto.getId()).getNoStoque();

	    // Salva o produto e captura o estoque atualizado
	    Produto salvarProduto = produtoServico.save(produto);

	    // Captura o estoque atualizado após salvar o produto
	    String quantidadeEstoqueAtual = salvarProduto.getNoStoque();

	    // Cria uma nova instância do InventarioProduto para esta modificação
	    Usuario usuario = getUsuario();
	    if (usuario == null) {
	        ra.addFlashAttribute("errorMessage", "Por favor, faça login para continuar.");
	        return "redirect:/login";
	    }

	    String rolesAsString = usuario.getRoles().stream()
	        .map(Role::getNome)
	        .reduce((role1, role2) -> role1 + ", " + role2)
	        .orElse("Sem Papel");

	    String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

	    String descricaoInventario = isNovo ? "Adição do Produto" : "Atualização do Produto";

	    // Garante que estamos criando um novo registro de inventário
	    InventarioProduto inventario = new InventarioProduto(null, usuario, salvarProduto, quantidadeEstoqueAtual, rolesAsString, descricaoInventario, dataFormatada, quantidadeEstoqueAnterior);
	    
	    // Salva o novo registro de inventário
	    inventarioProdutoService.salvaRegistroInventario(inventario);

	    ra.addFlashAttribute("mensagem", "O produto foi salvo com sucesso.");
	    return "redirect:/produtos";
	}



	
	private void deletarImagensExtrasRemovidasDoForm(Produto produto) {
		String imagensExtraProdutoDir = "produto-imagens/" + produto.getId() + "/extras";
		Path dirPath = Paths.get(imagensExtraProdutoDir);

		try {
			Files.list(dirPath).forEach(file -> {
				String filename = file.toFile().getName();

				if (!produto.contemImagemNome(filename)) {
					try {
						Files.delete(file);
						LOGGER.info("Imagem extra deletada: " + filename);

					} catch (IOException e) {
						LOGGER.error("Não foi possível deletar a imagem extra: " + filename);
					}
				}
			});
		} catch (IOException ex) {
			LOGGER.error("Não foi possível deletar o diretório: " + dirPath);
		}

	}

	private void setImagensExtraNomesExistentes(String[] imagemIDs, String[] imagemNomes, Produto produto) {

		if (imagemIDs == null || imagemIDs.length == 0)	return;

		Set<ProdutoImagem> imagens = new HashSet<>();
		for (int count = 0; count < imagemIDs.length; count++) {
			Integer id = Integer.parseInt(imagemIDs[count]);
			String nome = imagemNomes[count];

			imagens.add(new ProdutoImagem(id, nome, produto));
		}

		produto.setImagens(imagens);
	}

	private void setProdutoDetalhes(String[] detalhesIDs, String[] detalhesNomes, String[] detalhesValor, Produto produto) {
		if (detalhesNomes == null || detalhesNomes.length == 0)
			return;

		for (int count = 0; count < detalhesNomes.length; count++) {
			String nome = detalhesNomes[count];
			String valor = detalhesValor[count];
			Integer id = Integer.parseInt(detalhesIDs[count]);

			if (id != 0) {
				produto.addDetalhes(id, nome, valor);
			} else if (!nome.isEmpty() && !valor.isEmpty()) {
				produto.addDetalhes(nome, valor);
			}
		}
	}

	private void saveUploadedImages(MultipartFile imagemPrincipalMultipart,
			MultipartFile[] imagemExtraMultiparts, Produto salvarProduto) throws IOException {

		if (!imagemPrincipalMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(imagemPrincipalMultipart.getOriginalFilename());
			String uploadDir = "produto-imagens/" + salvarProduto.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, imagemPrincipalMultipart);
		}

		if (imagemExtraMultiparts.length > 0) {
			String uploadDir = "produto-imagens/" + salvarProduto.getId() + "/extras";

			for (MultipartFile multipartFile : imagemExtraMultiparts) {
				if (!multipartFile.isEmpty()) {

					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				}
			}
		}
	}

	private void setNewImagemExtraNome(MultipartFile[] imagemExtraMultiparts, Produto produto) {
		if (imagemExtraMultiparts.length > 0) {
			for (MultipartFile multipartFile : imagemExtraMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

					if (!produto.contemImagemNome(fileName)) {
						produto.addImagemExtra(fileName);
					}
				}
			}
		}
	}

	private void setImagemPrincipalNome(MultipartFile imagemPrincipalMultipart, Produto produto) {
		if (!imagemPrincipalMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(imagemPrincipalMultipart.getOriginalFilename());
			produto.setImagem_principal(fileName);
		}
	}

	@GetMapping("/produtos/{id}/habilitado/{status}")
	public String updateCategoriaStatus(@PathVariable("id") Integer id,
	        @PathVariable("status") boolean habilitado, RedirectAttributes redirectAttributes) throws ProdutoNotFoundException {
	    
	    Produto produto;
	    try {
	        produto = produtoServico.get(id);
	    } catch (ProdutoNotFoundException e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Produto não encontrado.");
	        return "redirect:/produtos";
	    }

	    produtoServico.updatePordutoHabilitadoStatus(id, habilitado);
	    
	    String status = habilitado ? "habilitado" : "desabilitado";
	    String mensagem = "O produto de id " + id + " foi " + status;
	    redirectAttributes.addFlashAttribute("mensagem", mensagem);

	   
	    String quantidadeEstoque = produto.getNoStoque();
	    
	   
	    Usuario usuario = getUsuario();
	    if (usuario == null) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Por favor, faça login para continuar.");
	        return "redirect:/login";
	    }

	  
	    String descricaoInventario = habilitado ? "Produto Ativado" : "Produto Desativado";

	 
	    String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

	  
	    String quantidadeEstoqueAnterior = produtoServico.get(id).getNoStoque();

	    
	    String rolesAsString = usuario.getRoles().stream()
	            .map(Role::getNome)
	            .reduce((role1, role2) -> role1 + ", " + role2)
	            .orElse("Sem Papel");

	 
	    InventarioProduto inventario = new InventarioProduto(null, usuario, produto, quantidadeEstoque, rolesAsString,descricaoInventario,dataFormatada,quantidadeEstoqueAnterior );
	    
	    inventarioProdutoService.salvaRegistroInventario(inventario);

	    return "redirect:/produtos";
	}


	@GetMapping("/produtos/deletar/{id}")
	public String deletarProduto(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra)
			throws IOException {
		try {
			produtoServico.deletar(id);
			String imagemProdutoDir = "produto-imagens/" + id;
			String imagensExtraProdutoDir = "produto-imagens/" + id + "/extras";

			FileUploadUtil.deleteDir(imagemProdutoDir);
			FileUploadUtil.deleteDir(imagensExtraProdutoDir);

			ra.addFlashAttribute("message", "O Produto ID " + id + " foi deletada com sucesso");
		} catch (ProdutoNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/produtos";
	}

	@GetMapping("/produtos/editar/{id}")
	public String editarProdutos(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			Produto produto = produtoServico.get(id);
			List<Marca> listMarcas = marcaServico.listAll();
			Integer numeroDeImagensExtras = produto.getImagens().size();

			model.addAttribute("produto", produto);
			model.addAttribute("listMarcas", listMarcas);
			model.addAttribute("tituloDaPag", "Editar produto (ID: " + id + ")");
			model.addAttribute("numeroDeImagensExtras", numeroDeImagensExtras);

			System.out.println("aqui1");

			return "produto_form";

		} catch (ProdutoNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			System.out.println("aqui2");

			return "redirect:/produtos";
		}

	}

	@GetMapping("/produtos/detalhes/{id}")
	public String verDetalhesProdutos(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Produto produto = produtoServico.get(id);
			model.addAttribute("produto", produto);

			return "produto_detalhes_modal";

		} catch (ProdutoNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return "redirect:/produtos";
		}
	}
}
