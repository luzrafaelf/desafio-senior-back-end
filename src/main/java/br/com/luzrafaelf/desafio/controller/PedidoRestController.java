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

import br.com.luzrafaelf.desafio.domain.PedidoDTO;
import br.com.luzrafaelf.desafio.domain.PedidoDescontoDTO;
import br.com.luzrafaelf.desafio.service.PedidoService;

@RestController
@RequestMapping(value = "/rest/pedidos")
public class PedidoRestController {

	@Autowired
	private PedidoService pedidoService;

	@GetMapping
	private Page<PedidoDTO> findAll(@RequestParam(name = "$page") Integer pageNumber, @RequestParam(name = "$quantity") Integer quantity) {
		PageRequest pageable = PageRequest.of(pageNumber, quantity);
		return pedidoService.findAll(pageable);
	};

	@GetMapping("/{id}")
	private PedidoDTO findOne(@PathVariable String id) {
		return pedidoService.findOne(id);
	};

	@PostMapping
	private PedidoDTO create(@RequestBody @Valid PedidoDTO entity) {
		return pedidoService.create(entity);
	};

	@PutMapping("/{id}")
	private PedidoDTO update(@PathVariable String id, @RequestBody @Valid PedidoDTO entity) {
		return pedidoService.update(id, entity);
	};

	@DeleteMapping("/{id}")
	private void delete(@PathVariable String id) {
		pedidoService.remove(id);
	};

	@PutMapping("/{id}/desconto")
	private PedidoDTO atribuirDesconto(@PathVariable String id, @RequestBody PedidoDescontoDTO desconto) {
		return pedidoService.atribuirDesconto(id, desconto);
	};
}
