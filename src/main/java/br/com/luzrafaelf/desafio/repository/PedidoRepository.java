package br.com.luzrafaelf.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luzrafaelf.desafio.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, String> {

}
