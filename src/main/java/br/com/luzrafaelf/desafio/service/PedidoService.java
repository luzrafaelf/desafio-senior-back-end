package br.com.luzrafaelf.desafio.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.luzrafaelf.desafio.domain.PedidoDTO;
import br.com.luzrafaelf.desafio.domain.PedidoDescontoDTO;
import br.com.luzrafaelf.desafio.domain.PedidoItemDTO;
import br.com.luzrafaelf.desafio.model.Pedido;
import br.com.luzrafaelf.desafio.model.PedidoItem;
import br.com.luzrafaelf.desafio.model.SituacaoPedido;
import br.com.luzrafaelf.desafio.model.TipoProduto;
import br.com.luzrafaelf.desafio.repository.PedidoRepository;

@Service
@Transactional
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PedidoItemService pedidoItemService;

	@Autowired
	private ProdutoService produtoService;

	protected PedidoDTO entityToDTO(Pedido entity) {
		PedidoDTO dto = new PedidoDTO();
		dto.setId(entity.getId());
		dto.setSituacao(entity.getSituacao());
		dto.setValorTotal(entity.getValorTotal());
		dto.setData(entity.getData());
		List<PedidoItemDTO> itens = entity.getItens().stream().map(i -> pedidoItemService.entityToDTO(i)).collect(Collectors.toList());
		dto.setItens(itens);
		return dto;
	}

	protected Pedido dtoToEntity(PedidoDTO dto) {
		Pedido entity = new Pedido();
		entity.setId(dto.getId());
		entity.setSituacao(dto.getSituacao());
		entity.setValorTotal(dto.getValorTotal());
		entity.setData(dto.getData());
		List<PedidoItem> itens = dto.getItens().stream().map(i -> {
			PedidoItem pedidoItem = pedidoItemService.dtoToEntity(i);
			pedidoItem.setPedido(entity);
			return pedidoItem;
		}).collect(Collectors.toList());
		entity.setItens(itens);
		return entity;
	}

	public Page<PedidoDTO> findAll(Pageable pageable, LocalDate dataInicial, LocalDate dataFinal, SituacaoPedido situacao) {
		Page<Pedido> findAll = pedidoRepository.findAllFiltered(dataInicial, dataFinal, situacao, pageable);
		List<PedidoDTO> listDtos = findAll.getContent().stream().map(p -> entityToDTO(p)).collect(Collectors.toList());
		return new PageImpl<>(listDtos, pageable, findAll.getTotalElements());
	}

	public PedidoDTO findOne(String id) {
		return findOneById(id);
	}

	public PedidoDTO create(PedidoDTO dto) {
		Pedido entity = pedidoRepository.save(dtoToEntity(dto));
		calcularTotal(entity);
		return entityToDTO(entity);
	}

	public PedidoDTO update(String id, PedidoDTO dto) {
		dto.setId(id);
		PedidoDTO pedido = findOneById(id);

		dto.setItens(pedido.getItens());
		Pedido entity = pedidoRepository.save(dtoToEntity(dto));
		calcularTotal(entity);
		return entityToDTO(entity);
	}

	public void remove(String id) {
		Pedido pedido = dtoToEntity(findOneById(id));
		pedidoRepository.delete(pedido);
	};

	private PedidoDTO findOneById(String id) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		if (!optional.isPresent()) {
			throw new NoSuchElementException(String.format("Não foi encontrado o pedido com o id '%s'", id));
		}
		Pedido pedido = optional.get();
		return entityToDTO(pedido);
	}

	public PedidoDTO atribuirDesconto(String id, PedidoDescontoDTO desconto) {

		PedidoDTO dto = findOneById(id);

		if (dto.getSituacao() != SituacaoPedido.ABERTO) {
			throw new IllegalArgumentException("Não pode atribuir desconto para um pedido que não está com a situação em aberto");
		}

		Pedido entity = dtoToEntity(dto);
		calcularTotal(desconto.getPercentualDesconto(), entity);
		entity = pedidoRepository.save(entity);

		return entityToDTO(entity);
	}

	private void calcularTotal(Pedido entity) {
		calcularTotal(BigDecimal.ZERO, entity);
	}

	private void calcularTotal(BigDecimal percentualDesconto, Pedido entity) {
		BigDecimal valorTotalProdutos = entity.getItens().stream().filter(i -> i.getProduto().getTipo() == TipoProduto.PRODUTO).map(i -> i.getProduto().getValorCusto()).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal valorTotalServicos = entity.getItens().stream().filter(i -> i.getProduto().getTipo() == TipoProduto.SERVICO).map(i -> i.getProduto().getValorCusto()).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal valorDesconto = percentualDesconto.multiply(valorTotalProdutos).multiply(new BigDecimal("0.01"));
		entity.setValorTotal(valorTotalProdutos.add(valorTotalServicos).subtract(valorDesconto));
	}

	public PedidoDTO adicionarItem(String id, PedidoItemDTO item) {

		if (!produtoService.findOne(item.getProduto().getId()).getAtivo()) {
			throw new ValidationException("Produtos inativos não podem ser adicionados ao pedido");
		}

		PedidoDTO dto = findOne(id);
		dto.getItens().add(item);

		Pedido entity = dtoToEntity(dto);
		calcularTotal(entity);
		entity = pedidoRepository.save(entity);
		return entityToDTO(entity);
	}

	public PedidoDTO removerItem(String id, PedidoItemDTO item) {
		PedidoDTO dto = findOne(id);

		List<PedidoItemDTO> itens = dto.getItens().stream().filter(i -> !i.getId().equals(item.getId())).collect(Collectors.toList());
		dto.getItens().clear();
		dto.setItens(itens);

		Pedido entity = dtoToEntity(dto);
		calcularTotal(entity);
		entity = pedidoRepository.save(entity);
		return entityToDTO(entity);
	}

	public PedidoDTO atualizarItem(String id, PedidoItemDTO item) {

		PedidoDTO dto = findOne(id);
		for (PedidoItemDTO i : dto.getItens()) {
			if (i.getId().equals(id)) {
				i = item;
				break;
			}
		}

		Pedido entity = dtoToEntity(dto);
		entity = pedidoRepository.save(entity);
		return entityToDTO(entity);
	}
}
