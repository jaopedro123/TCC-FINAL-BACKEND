package com.MotherBoard.entidade.comum;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
	
	@Column(length = 128, nullable = false)
	private String imagem;
	
	private boolean habilitado;
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Categoria parent;
	
	@OneToMany(mappedBy = "parent")
	private Set<Categoria> children = new HashSet<>();

	
	
	public Categoria(Integer id) {
		this.id = id;
	}

	public Categoria(String nome) {
		this.nome = nome;
		this.alias = nome;
		this.imagem = "default.png";
	}
	
	public Categoria(String nome, Categoria parent) {
		this(nome);
		this.parent = parent;
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

	public Categoria getParent() {
		return parent;
	}

	public void setParent(Categoria parent) {
		this.parent = parent;
	}

	public Set<Categoria> getChildren() {
		return children;
	}

	public void setChildren(Set<Categoria> children) {
		this.children = children;
	}
	
	
}
