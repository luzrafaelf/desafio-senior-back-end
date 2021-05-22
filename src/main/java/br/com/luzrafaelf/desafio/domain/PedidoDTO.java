package br.com.luzrafaelf.desafio.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.luzrafaelf.desafio.model.SituacaoPedido;

public class PedidoDTO {

	private String id;

	private List<PedidoItemDTO> itens = new ArrayList<>(0);

	private SituacaoPedido situacao;

	private BigDecimal valorTotal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PedidoItemDTO> getItens() {
		return itens;
	}

	public void setItens(List<PedidoItemDTO> itens) {
		this.itens = itens;
	}

	public SituacaoPedido getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPedido situacao) {
		this.situacao = situacao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

}
