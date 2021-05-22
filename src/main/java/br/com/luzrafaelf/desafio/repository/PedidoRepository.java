package br.com.luzrafaelf.desafio.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.luzrafaelf.desafio.model.Pedido;
import br.com.luzrafaelf.desafio.model.SituacaoPedido;

public interface PedidoRepository extends JpaRepository<Pedido, String> {

	@Query("select p from Pedido p where (:situacao is null or p.situacao = :situacao) and (coalesce(:dataInicial, null) is null or p.data >= :dataInicial) and (coalesce(:dataFinal, null) is null or p.data <= :dataFinal)")
	Page<Pedido> findAllFiltered(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal, @Param("situacao") SituacaoPedido situacao, Pageable pageable);

}
