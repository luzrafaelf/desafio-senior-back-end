package br.com.luzrafaelf.desafio.domain;

import java.math.BigDecimal;

import br.com.luzrafaelf.desafio.model.TipoProduto;

public class ProdutoDTO {

	private String id;

	private TipoProduto tipo;

	private Boolean ativo;

	private BigDecimal valorCusto;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TipoProduto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProduto tipo) {
		this.tipo = tipo;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

}
