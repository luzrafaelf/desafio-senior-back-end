package br.com.luzrafaelf.desafio.domain;

public class PedidoItemDTO {

	private String id;

	private ProdutoDTO produto;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProdutoDTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoDTO produto) {
		this.produto = produto;
	}

}
