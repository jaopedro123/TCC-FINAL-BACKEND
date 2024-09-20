package com.MotherBoard.Admin.produto;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.Produto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProdutoServico {

	@Autowired
	private ProdutoRepositorio repo;

	public List<Produto> listAll() {
		return (List<Produto>) repo.findAll();

	}

	public Produto save(Produto produto) {
		if (produto.getId() == null) {
			produto.setTempoDaCriacao(new Date());
		}
		if (produto.getAlias() == null || produto.getAlias().isEmpty()) {
			String aliasPadrao = produto.getNome().replaceAll(" ", "-");
			produto.setAlias(aliasPadrao);
		} else {
			produto.setAlias(produto.getAlias().replaceAll(" ", "-"));
		}
		produto.setTempoAtualizado(new Date());
		return repo.save(produto);
	}

	public String checkUniqueProduto(Integer id, String nome) {
		boolean eumacriacaonova = (id == null || id == 0);
		Produto produtoByNome = repo.findByNome(nome);
		if (eumacriacaonova) {
			if (produtoByNome != null)
				return "Duplicado";
		} else {
			if (produtoByNome != null && produtoByNome.getId() != id) {
				return "Duplicado";
			}
		}
		return "OK";
	}

	public void updatePordutoHabilitadoStatus(Integer id, boolean habilitado) {
		repo.updateHabilitadoStatus(id, habilitado);
	}

	public void deletar(Integer id) throws ProdutoNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new ProdutoNotFoundException("Não foi possível achar o Produto com ID " + id);
		}

		repo.deleteById(id);
	}

	public Produto get(Integer id) throws ProdutoNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex){
			throw new ProdutoNotFoundException("Não foi possível achar nenhum produto com ID " + id);
		}
	}
	
}
