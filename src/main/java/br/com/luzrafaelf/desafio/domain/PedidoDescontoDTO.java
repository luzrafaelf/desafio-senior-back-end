package br.com.luzrafaelf.desafio.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.lang.NonNull;

public class PedidoDescontoDTO {

	@NonNull
	@Max(100)
	@Min(0)
	private BigDecimal percentualDesconto;

	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

}
