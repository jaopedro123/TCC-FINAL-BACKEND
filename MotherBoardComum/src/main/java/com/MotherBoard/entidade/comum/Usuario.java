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
@Table(name = "usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	@Column(length = 64, nullable = false)
	private String senha;
	@Column(length = 128, nullable = false)
	private String nomeCompleto;
	
	@Column(length = 14, nullable = false, unique = true)
	private String cpf;
	
	@Column(length = 64)
	private String fotos;
	private boolean permitido;
	
	@ManyToMany
	@JoinTable(
			name = "roles_usuarios",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles = new HashSet<>();
	
	
	public Usuario() {
		
	}

	public Usuario(String email, String senha, String nomeCompleto, String cpf) {
		this.email = email;
		this.senha = senha;
		this.nomeCompleto = nomeCompleto;
		this.cpf = cpf;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getFotos() {
		return fotos;
	}

	public void setFotos(String fotos) {
		this.fotos = fotos;
	}

	public boolean isPermitido() {
		return permitido;
	}

	public void setPermitido(boolean permitido) {
		this.permitido = permitido;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", nomeCompleto=" + nomeCompleto + ", cpf=" + cpf + ", roles="
				+ roles + "]";
	}
	
	
	@Transient
	public String getPhotosImagePath() {
	    if (id == null || fotos == null) return "/imagens/imagem-default.png";
	    
		System.out.println("/fotos-usuario/" + this.id + "/" + this.fotos);

	    return "/fotos-usuario/" + this.id + "/" + this.fotos;
	}

	
}
