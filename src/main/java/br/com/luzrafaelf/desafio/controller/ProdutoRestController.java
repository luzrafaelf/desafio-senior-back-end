package br.com.luzrafaelf.desafio.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.luzrafaelf.desafio.domain.ProdutoDTO;
import br.com.luzrafaelf.desafio.model.TipoProduto;
import br.com.luzrafaelf.desafio.service.ProdutoService;

@RestController
@RequestMapping(value = "/rest/produtos")
public class ProdutoRestController {

	@Autowired
	private ProdutoService produtoService;

	@GetMapping
	private Page<ProdutoDTO> findAll(
	//@formatter:off
			@RequestParam(name = "$page") Integer pageNumber, 
			@RequestParam(name = "$quantity") Integer quantity,
			@RequestParam(name = "tipo", required = false) TipoProduto tipo,
			@RequestParam(name = "ativo", required = false) Boolean ativo
		//@formatter:on
	) {
		PageRequest pageable = PageRequest.of(pageNumber, quantity);
		return produtoService.findAll(tipo, ativo, pageable);
	};

	@GetMapping("/{id}")
	private ProdutoDTO findOne(@PathVariable String id) {
		return produtoService.findOne(id);
	};

	@PostMapping
	private ProdutoDTO create(@Valid @RequestBody ProdutoDTO dto) {
		return produtoService.create(dto);
	};

	@PutMapping("/{id}")
	private ProdutoDTO update(@PathVariable String id, @Valid @RequestBody ProdutoDTO dto) {
		return produtoService.update(id, dto);
	};

	@DeleteMapping("/{id}")
	private void delete(@PathVariable String id) {
		produtoService.remove(id);
	};

}
