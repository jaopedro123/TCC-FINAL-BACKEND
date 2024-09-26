package com.MotherBoard.entidade.comum;

import jakarta.persistence.*;

@Entity
@Table(name = "InventarioCategoria")
public class InventarioCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")  
    private Usuario usuarioId;

    @ManyToOne
    @JoinColumn(name = "categoria_id") 
    private Categoria categoria;

    @Column(name = "role_usuario")  
    private String roleUsuario;

    @Column(name = "acao")  
    private String acao;

    @Column(name = "data_modificacao")  
    private String dataModificacao;

    public InventarioCategoria() {
    	
    }

	public InventarioCategoria(Integer id, Usuario usuarioId, Categoria categoria, String roleUsuario, String acao,
			String dataModificacao) {
		super();
		this.id = id;
		this.usuarioId = usuarioId;
		this.categoria = categoria;
		this.roleUsuario = roleUsuario;
		this.acao = acao;
		this.dataModificacao = dataModificacao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Usuario usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getRoleUsuario() {
		return roleUsuario;
	}

	public void setRoleUsuario(String roleUsuario) {
		this.roleUsuario = roleUsuario;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(String dataModificacao) {
		this.dataModificacao = dataModificacao;
	}




    
}
