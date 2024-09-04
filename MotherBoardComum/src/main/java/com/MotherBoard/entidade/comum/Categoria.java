package com.MotherBoard.entidade.comum;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "categorias")
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String nome;

	@Column(length = 64, nullable = false, unique = true)
	private String alias;
	
	@Column(length = 128)
	private String imagem;
	
	private boolean habilitado;
	
	@ManyToOne
	@JoinColumn(name = "pai_id", unique = false)
	private Categoria pai;
	
	@OneToMany(mappedBy = "pai")
	private Set<Categoria> filho = new HashSet<>();
	
	public Categoria() {
	}
	
	public Categoria(Integer id) {
		this.id = id;
	}

	public static Categoria copiarIdENome(Categoria categoria) {
		Categoria copiarCategoria = new Categoria();
		copiarCategoria.setId(categoria.getId());
		copiarCategoria.setNome(categoria.getNome());

		return copiarCategoria;
	}

	public static Categoria copiarIdENome(Integer id, String nome) {
		Categoria copiarCategoria = new Categoria();
		copiarCategoria.setId(id);
		copiarCategoria.setNome(nome);

		return copiarCategoria;
	}

	public static Categoria copiarTudo(Categoria categoria) {
		Categoria copiarCategoria = new Categoria();
		copiarCategoria.setId(categoria.getId());
		copiarCategoria.setNome(categoria.getNome());
		copiarCategoria.setImagem(categoria.getImagem());
		copiarCategoria.setAlias(categoria.getAlias());
		copiarCategoria.setHabilitado(categoria.isHabilitado());
		copiarCategoria.setHasChildren(categoria.getFilho().size() > 0);

		return copiarCategoria;
	}

	public static Categoria copiarTudo(Categoria categoria, String nome) { 
		Categoria copiarCategoria = Categoria.copiarTudo(categoria);
		copiarCategoria.setNome(nome);
		
		return copiarCategoria;
	}

	public Categoria(String nome) {
		this.nome = nome;
		this.alias = nome;
		//this.imagem = "/imagens/image-light.png";
	}
	
	public Categoria(String nome, Categoria pai) {
		this(nome);
		this.pai = pai;
	}

	public Categoria(Integer id, String nome, String alias) {
		this.id = id;
		this.nome = nome;
		this.alias = alias;
	}

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

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public Categoria getPai() {
		return pai;
	}

	public void setPai(Categoria pai) {
		this.pai = pai;
	}

	public Set<Categoria> getFilho() {
		return filho;
	}

	public void setFilho(Set<Categoria> filho) {
		this.filho = filho;
	}
	
	@Transient
	public String getImagemPath() {
	    if (id == null ||imagem == null) return "/imagens/imagem-default.png";

	    return "/categoria-imagens/" + this.id + "/" + this.imagem;
	}

	public boolean isHasChildren() {
		return temFilhos;
	}

	public void setHasChildren(boolean temFilhos) {
		this.temFilhos = temFilhos;
	}

	@Transient
	private boolean temFilhos; 
	
	
}
