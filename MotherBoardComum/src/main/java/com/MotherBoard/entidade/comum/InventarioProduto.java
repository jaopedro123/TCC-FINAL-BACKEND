package com.MotherBoard.entidade.comum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "InventarioProduto")
public class InventarioProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")  
    private Usuario usuarioId;

    @ManyToOne
    @JoinColumn(name = "produto_id") 
    private Produto produto;
    
    @Column(name = "estoque") 
    private String estoque;

    @Column(name = "role_usuario")  
    private String roleUsuario;

    @Column(name = "acao")  
    private String acao;

    @Column(name = "data_modificacao")  
    private String dataModificacao;
    
    @Column(name = "quantidadeEstoqueAnterior") 
    private String quantidadeEstoqueAnterior; 
   
    
    public InventarioProduto() {
    	
    }

	public InventarioProduto(Integer id, Usuario usuarioId, Produto produto, String estoque, String roleUsuario,
			String acao, String dataModificacao, String quantidadeEstoqueAnterior) {
		super();
		this.id = id;
		this.usuarioId = usuarioId;
		this.produto = produto;
		this.estoque = estoque;
		this.roleUsuario = roleUsuario;
		this.acao = acao;
		this.dataModificacao = dataModificacao;
		this.quantidadeEstoqueAnterior = quantidadeEstoqueAnterior;
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

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public String getEstoque() {
		return estoque;
	}

	public void setEstoque(String estoque) {
		this.estoque = estoque;
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
	
    public String getQuantidadeEstoqueAnterior() {
        return quantidadeEstoqueAnterior;
    }

    public void setQuantidadeEstoqueAnterior(String quantidadeEstoqueAnterior) {
        this.quantidadeEstoqueAnterior = quantidadeEstoqueAnterior;
    }
	
}
