package com.MotherBoard.entidade.comum;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "marcas")
public class Marca {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 45, unique = true)
	private String nome;
	
	@Column(nullable = false, length = 128)
	private String logo;
	
	@ManyToMany
	@JoinTable(name = "marcas_categorias", joinColumns = @JoinColumn(name = "marca_id"), inverseJoinColumns = @JoinColumn(name = "Categoria_id"))
	private Set<Categoria> categorias = new HashSet<>();

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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Marca(Integer id, String nome, String logo, Set<Categoria> categorias) {
		super();
		this.id = id;
		this.nome = nome;
		this.logo = logo;
		this.categorias = categorias;
	}

	public Marca() {
		
	}

	@Override
	public String toString() {
		return "Marca [id=" + id + ", nome=" + nome + ", logo=" + logo + ", categorias=" + categorias + "]";
	}
	
	@Transient
	public String getLogoPath() {
	    if (this.id == null) return "/imagens/image-thumbnail.png";
	    return "/Marca-logos/" + this.id + "/" + this.logo;
	}

	
}
