package com.MotherBoard.Admin.produto;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.MotherBoard.Admin.marca.MarcaServico;
import com.MotherBoard.entidade.comum.Marca;
import com.MotherBoard.entidade.comum.Produto;

@Controller
public class ProdutoControlador {
	@Autowired
	private ProdutoServico produtoServico;
	@Autowired
	private MarcaServico marcaServico;

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
	public String saveProduto(Produto produto, RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile imagemPrincipalMultipart,
			@RequestParam("imagemExtra") MultipartFile[] imagemExtraMultiparts)
			throws IOException {
		
		setImagemPrincipalNome(imagemPrincipalMultipart, produto);
		setImagemExtraNome(imagemExtraMultiparts, produto);

		Produto salvarProduto = produtoServico.save(produto); 

		saveUploadedImages(imagemPrincipalMultipart, imagemExtraMultiparts, salvarProduto);
		
		ra.addFlashAttribute("mensagem", "o produto foi salvo com sucesso");
		
		return "redirect:/produtos";
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
				if (!multipartFile.isEmpty()) continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			};
		}
	}

	private void setImagemExtraNome(MultipartFile[] imagemExtraMultiparts, Produto produto) {
		if (imagemExtraMultiparts.length > 0) {
			for (MultipartFile multipartFile : imagemExtraMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					produto.addImagemExtra(fileName);
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
			@PathVariable("status") boolean habilitado, RedirectAttributes redirectAttributes) {
		produtoServico.updatePordutoHabilitadoStatus(id, habilitado);
		String status = habilitado ? "habilitado" : "desabilitado";
		String mensagem = "o produto de id " + id + " foi " + status;
		redirectAttributes.addFlashAttribute("mensagem", mensagem);
		return "redirect:/produtos";
	};

	@GetMapping("/produtos/deletar/{id}")
	public String deletarProduto(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) throws IOException {
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
}
