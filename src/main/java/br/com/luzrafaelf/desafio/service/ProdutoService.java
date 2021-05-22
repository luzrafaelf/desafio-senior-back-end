package br.com.luzrafaelf.desafio.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.luzrafaelf.desafio.domain.ProdutoDTO;
import br.com.luzrafaelf.desafio.model.Produto;
import br.com.luzrafaelf.desafio.model.TipoProduto;
import br.com.luzrafaelf.desafio.repository.ProdutoRepository;

@Service
@Transactional
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	protected ProdutoDTO entityToDTO(Produto entity) {
		ProdutoDTO dto = new ProdutoDTO();
		dto.setId(entity.getId());
		dto.setTipo(entity.getTipo());
		dto.setValorCusto(entity.getValorCusto());
		dto.setAtivo(entity.getAtivo());
		return dto;
	}

	protected Produto dtoToEntity(ProdutoDTO dto) {
		Produto entity = new Produto();
		entity.setId(dto.getId());
		entity.setTipo(dto.getTipo());
		entity.setValorCusto(dto.getValorCusto());
		entity.setAtivo(dto.getAtivo());
		return entity;
	}

	public Page<ProdutoDTO> findAll(TipoProduto tipo, Boolean ativo, Pageable pageable) {
		Page<Produto> findAll = produtoRepository.findAllByTipo(tipo, ativo, pageable);
		List<ProdutoDTO> listDtos = findAll.getContent().stream().map(p -> entityToDTO(p)).collect(Collectors.toList());
		return new PageImpl<>(listDtos, pageable, findAll.getTotalElements());
	}

	public ProdutoDTO findOne(String id) {
		return findOneById(id);
	}

	public ProdutoDTO create(ProdutoDTO dto) {
		Produto entity = produtoRepository.save(dtoToEntity(dto));
		return entityToDTO(entity);
	}

	public ProdutoDTO update(String id, ProdutoDTO dto) {
		dto.setId(id);
		Produto entity = produtoRepository.save(dtoToEntity(dto));
		return entityToDTO(entity);
	}

	public void remove(String id) {
		Produto pedido = dtoToEntity(findOneById(id));
		produtoRepository.delete(pedido);
	};

	private ProdutoDTO findOneById(String id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (!optional.isPresent()) {
			throw new NoSuchElementException(String.format("Não foi encontrado o produto com o id '%s'", id));
		}
		Produto pedido = optional.get();
		return entityToDTO(pedido);
	}

}
