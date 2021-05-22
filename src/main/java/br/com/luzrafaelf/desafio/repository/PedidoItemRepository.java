package br.com.luzrafaelf.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luzrafaelf.desafio.model.PedidoItem;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, String> {

}
