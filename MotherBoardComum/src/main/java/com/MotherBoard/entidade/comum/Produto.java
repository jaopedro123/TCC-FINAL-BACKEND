package com.MotherBoard.entidade.comum;

import java.beans.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "produtos")
public class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 256, nullable = false, unique = true)
	private String nome;

	@Column(length = 256, nullable = false, unique = false)
	private String alias;

	@Column(length = 12288, nullable = false, name = "descricao_curta")
	private String descricaoCurta;

	@Column(length = 12288, nullable = false, name = "descricao_completa")
	private String descricaoCompleta;

	@Column(name = "tempo_daCriacao")
	private Date tempoDaCriacao;

	@Column(name = "tempo_atualizado")
	private Date tempoAtualizado;

	private boolean habilitado;

	@Column(name = "no_stoque")
	private String noStoque;

	private String custo;

	private String preco;

	@Column(name = "des_conto")
	private String desconto;

	@Column(name = "imagem_principal", nullable = false)
	private String imagem_principal;

	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	@ManyToOne
	@JoinColumn(name = "marca_id")
	private Marca marca;

	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProdutoImagem> imagens = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescricaoCurta() {
		return descricaoCurta;
	}

	public void setDescricaoCurta(String descricaoCurta) {
		this.descricaoCurta = descricaoCurta;
	}

	public String getDescricaoCompleta() {
		return descricaoCompleta;
	}

	public void setDescricaoCompleta(String descricaoCompleta) {
		this.descricaoCompleta = descricaoCompleta;
	}

	public Date getTempoDaCriacao() {
		return tempoDaCriacao;
	}

	public void setTempoDaCriacao(Date tempoDaCriacao) {
		this.tempoDaCriacao = tempoDaCriacao;
	}

	public Date getTempoAtualizado() {
		return tempoAtualizado;
	}

	public void setTempoAtualizado(Date tempoAtualizado) {
		this.tempoAtualizado = tempoAtualizado;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public String getNoStoque() {
		return noStoque;
	}

	public void setNoStoque(String noStoque) {
		this.noStoque = noStoque;
	}

	public String getCusto() {
		return custo;
	}

	public void setCusto(String custo) {
		this.custo = custo;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id + ", nome=" + nome + "]";
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
	}

	public void setInStock(boolean b) {
		// TODO Auto-generated method stub
	}

	public String getImagem_principal() {
		return imagem_principal;
	}

	public void setImagem_principal(String imagem_principal) {
		this.imagem_principal = imagem_principal;
	}

	public Set<ProdutoImagem> getImagens() {
		return imagens;
	}

	public void setImagens(Set<ProdutoImagem> imagens) {
		this.imagens = imagens;
	}

	public void addImagemExtra(String nomeImagem) {
		this.imagens.add(new ProdutoImagem(nomeImagem, this));
	} 

	public String getImagemPrincipalPath() {
		if (id == null || imagem_principal == null) return "/imagens/image-thumbnail.png";

		return "/produto-imagens/" + this.id + "/" + this.imagem_principal;
	}

	public boolean contemImagemNome(String imagemNome) {
		Iterator<ProdutoImagem> iterator = imagens.iterator();

		while (iterator.hasNext()) {
			ProdutoImagem imagem = iterator.next();

			if (imagem.getNome().equals(imagemNome)) {

				return true;
			}
		}	

		return false;
	}

	@Transient
	public String getNomeCurto() {
		if (nome.length() > 50) {
			return nome.substring(0, 50).concat("...");
		}
		return nome;
	}

}
