package com.MotherBoard.Admin.produto;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.Produto;

import jakarta.transaction.Transactional;




@Service
@Transactional
public class ProdutoServico {

	@Autowired private ProdutoRepositorio repo;
	
	public List<Produto> listAll(){
		return (List<Produto>)repo.findAll();
		
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
		if(eumacriacaonova) {
			if (produtoByNome != null) return "Duplicado";
		}else {
			if (produtoByNome != null && produtoByNome.getId() != id) { return "Duplicado";
			}
		}
		return "OK";
	}
	public  void updatePordutoHabilitadoStatus(Integer id, boolean habilitado) {
		repo.updateHabilitadoStatus(id,habilitado);
	}
}
