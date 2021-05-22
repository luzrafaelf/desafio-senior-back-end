package br.com.luzrafaelf.desafio.model;

import java.math.BigDecimal;
import java.util.List;

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
@Table(name = "produto")
public class Produto {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@NonNull
	@Column(name = "tipo", nullable = false)
	private TipoProduto tipo;

	@NonNull
	@Column(name = "ativo", nullable = false)
	private Boolean ativo;

	@OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
	private List<PedidoItem> pedidoItens;

	@Min(0)
	@Column(name = "valor_custo", nullable = false)
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

	public List<PedidoItem> getPedidoItens() {
		return pedidoItens;
	}

	public void setPedidoItens(List<PedidoItem> pedidoItens) {
		this.pedidoItens = pedidoItens;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

}
