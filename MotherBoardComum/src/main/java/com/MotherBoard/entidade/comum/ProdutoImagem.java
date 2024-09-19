package com.MotherBoard.entidade.comum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "produtos_imagem")
public class ProdutoImagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public ProdutoImagem() {
    }

    public ProdutoImagem(Integer id, String nome, Produto produto) {
        this.id = id;
        this.nome = nome;
        this.produto = produto;
    }

    public ProdutoImagem(String nome, Produto produto) {
        this.nome = nome;
        this.produto = produto;
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

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Transient
    public String getImagemPath() {
        return "/produto-imagens/" + produto.getId() + "/extras/" + this.nome;
    }

}
