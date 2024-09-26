package com.MotherBoard.entidade.comum;


import jakarta.persistence.*;

@Entity
@Table(name = "InventarioMarca")
public class InventarioMarca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")  
    private Usuario usuarioId;

    @ManyToOne
    @JoinColumn(name = "marca_id") 
    private Marca marca;

    @Column(name = "role_usuario")  
    private String roleUsuario;

    @Column(name = "acao")  
    private String acao;

    @Column(name = "data_modificacao")  
    private String dataModificacao;

    public InventarioMarca() {
    	
    }


    public InventarioMarca(Integer id, Usuario usuarioId, Marca marca, String roleUsuario, String dataModificacao, String acao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.marca = marca;
        this.roleUsuario = roleUsuario;
        this.dataModificacao = dataModificacao;
        this.acao = acao;
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

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
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
