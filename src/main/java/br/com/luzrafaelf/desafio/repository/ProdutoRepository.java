package br.com.luzrafaelf.desafio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.luzrafaelf.desafio.model.Produto;
import br.com.luzrafaelf.desafio.model.TipoProduto;

public interface ProdutoRepository extends JpaRepository<Produto, String> {

	@Query("select p from Produto p where (:tipo is null or p.tipo = :tipo) and (:ativo is null or p.ativo = :ativo)")
	Page<Produto> findAllByTipo(@Param("tipo") TipoProduto tipo, @Param("ativo") Boolean ativo, Pageable pageable);

}
