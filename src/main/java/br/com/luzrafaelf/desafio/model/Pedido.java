package br.com.luzrafaelf.desafio.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "pedido")
public class Pedido {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<PedidoItem> itens;

	@NonNull
	@Column(name = "situacao", nullable = false)
	private SituacaoPedido situacao;

	@Min(0)
	@NonNull
	@Column(name = "valor_total", nullable = false)
	private BigDecimal valorTotal;

	@Column(name = "data", nullable = false)
	private LocalDate data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PedidoItem> getItens() {
		return itens;
	}

	public void setItens(List<PedidoItem> itens) {
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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

}
