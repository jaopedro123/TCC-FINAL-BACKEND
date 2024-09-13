package com.MotherBoard.entidade.comum;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "produtos")
public class Produto {
	       @Id
	       @GeneratedValue(strategy = GenerationType.IDENTITY)
           private Integer id;
	     
	       @Column(length = 256, nullable = false, unique = true)
           private String nome;
	       
	       @Column(length = 256, nullable = false, unique = true)
           private String alias;
	       
	       @Column(length = 512, nullable = false, name = "descricao_curta")
           private String descricaoCurta;
	       
	       @Column(length = 4096, nullable = false, name = "descricao_completa")
           private String descricaoCompleta;
           
	       @Column(name = "tempo_daCriacao")
           private Date tempoDaCriacao;
	       
	       @Column(name = "tempo_atualizado")
           private Date tempoAtualizado;
           
           private boolean habilitado;
           
           @Column(name = "no_stoque")
           private boolean noStoque;
           
           private float custo;
           
           private float preco;
           
           @Column(name = "des_conto")
           private float desconto;
           
           private float comprimento;
           private float largura;
           private float altura;
           private float peso;
           
           @ManyToOne
           @JoinColumn(name = "categoria_id")
           private Categoria categoria;
           
           @ManyToOne
           @JoinColumn(name = "marca_id")
            private Marca marca;
           
           
           
           
           
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

		public boolean isNoStoque() {
			return noStoque;
		}

		public void setNoStoque(boolean noStoque) {
			this.noStoque = noStoque;
		}

		public float getCusto() {
			return custo;
		}

		public void setCusto(float custo) {
			this.custo = custo;
		}

		public float getPreco() {
			return preco;
		}

		public void setPreco(float preco) {
			this.preco = preco;
		}

		public float getDesconto() {
			return desconto;
		}

		public void setDesconto(float desconto) {
			this.desconto = desconto;
		}

		public float getComprimento() {
			return comprimento;
		}

		public void setComprimento(float comprimento) {
			this.comprimento = comprimento;
		}

		public float getLargura() {
			return largura;
		}

		public void setLargura(float largura) {
			this.largura = largura;
		}

		public float getAltura() {
			return altura;
		}

		public void setAltura(float altura) {
			this.altura = altura;
		}

		public float getPeso() {
			return peso;
		}

		public void setPeso(float peso) {
			this.peso = peso;
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
           
         
           
           
}
