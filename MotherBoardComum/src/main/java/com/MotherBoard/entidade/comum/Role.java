package com.MotherBoard.entidade.comum;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

	@jakarta.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	@Column(length = 40, nullable = false, unique = true)
	private String nome;
	
	@Column(length = 500, nullable = false)
	private String Descricao;
	
	public Role() {
		super();
	}

	public Role(Integer id) {
		this.Id = id;
	}
	
	public Role(String nome) {
		super();
		this.nome = nome;
	}
	
	

	public Role(String nome, String descricao) {
		super();
		this.nome = nome;
		this.Descricao = descricao;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return Descricao;
	}

	public void setDescricao(String descricao) {
		Descricao = descricao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(Id, other.Id);
	}
/*
	@Override
	public String toString() {
		return "Role [nome=" + nome + "]";
	}

*/	
	@Override
	public String toString() {
		return this.nome;
	}
	

}
