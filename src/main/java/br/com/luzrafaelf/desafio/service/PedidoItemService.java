package br.com.luzrafaelf.desafio.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luzrafaelf.desafio.domain.PedidoItemDTO;
import br.com.luzrafaelf.desafio.model.PedidoItem;
import br.com.luzrafaelf.desafio.repository.ProdutoRepository;

@Service
@Transactional
public class PedidoItemService {

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	public PedidoItemDTO entityToDTO(PedidoItem entity) {
		PedidoItemDTO dto = new PedidoItemDTO();
		dto.setId(entity.getId());
		dto.setProduto(produtoService.findOne(entity.getProduto().getId()));
		return dto;
	}

	public PedidoItem dtoToEntity(PedidoItemDTO dto) {
		PedidoItem entity = new PedidoItem();
		entity.setId(entity.getId());
		entity.setProduto(produtoRepository.findById(dto.getProduto().getId()).get());
		return entity;
	}

}
