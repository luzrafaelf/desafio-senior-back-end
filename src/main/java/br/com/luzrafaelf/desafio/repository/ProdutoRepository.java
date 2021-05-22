package br.com.luzrafaelf.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luzrafaelf.desafio.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, String> {

}
