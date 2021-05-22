package br.com.luzrafaelf.desafio.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import br.com.luzrafaelf.desafio.DesafioApplication;
import br.com.luzrafaelf.desafio.domain.PedidoDTO;
import br.com.luzrafaelf.desafio.domain.PedidoDescontoDTO;
import br.com.luzrafaelf.desafio.domain.PedidoItemDTO;
import br.com.luzrafaelf.desafio.domain.ProdutoDTO;
import br.com.luzrafaelf.desafio.model.SituacaoPedido;
import br.com.luzrafaelf.desafio.model.TipoProduto;
import br.com.luzrafaelf.desafio.service.PedidoService;
import br.com.luzrafaelf.desafio.service.ProdutoService;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = DesafioApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc
public class Tests {

	@Autowired
	private ProdutoService produtoService;

	@Test
	public void createProduto() throws Exception {

		ProdutoDTO dto = new ProdutoDTO();
		dto.setTipo(TipoProduto.PRODUTO);
		dto.setAtivo(true);
		dto.setValorCusto(new BigDecimal("100.00"));
		dto = produtoService.create(dto);

		assertNotNull(dto.getId());

		ProdutoDTO produtoNaBase = produtoService.findOne(dto.getId());

		assertEquals(true, produtoNaBase.getAtivo());
		assertEquals(TipoProduto.PRODUTO, produtoNaBase.getTipo());
		assertEquals(0, produtoNaBase.getValorCusto().compareTo(new BigDecimal("100.00")));

	}

	@Test
	public void deleteProduto() throws Exception {

		ProdutoDTO dto = new ProdutoDTO();
		dto.setTipo(TipoProduto.PRODUTO);
		dto.setAtivo(true);
		dto.setValorCusto(new BigDecimal("100.00"));
		dto = produtoService.create(dto);

		ProdutoDTO produtoNaBase = produtoService.findOne(dto.getId());
		assertNotNull(produtoNaBase);

		String id = produtoNaBase.getId();
		produtoService.remove(id);

		NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
			produtoService.findOne(id);
		});

		assertTrue(thrown.getMessage().equals("Não foi encontrado o produto com o id '" + id + "'"));

	}

	@Test
	public void updateProduto() throws Exception {

		ProdutoDTO dto = new ProdutoDTO();
		dto.setTipo(TipoProduto.PRODUTO);
		dto.setAtivo(true);
		dto.setValorCusto(new BigDecimal("100.00"));
		dto = produtoService.create(dto);

		dto.setTipo(TipoProduto.SERVICO);
		dto.setAtivo(false);
		dto.setValorCusto(new BigDecimal("5.00"));
		produtoService.update(dto.getId(), dto);

		ProdutoDTO produtoNaBase = produtoService.findOne(dto.getId());
		assertEquals(false, produtoNaBase.getAtivo());
		assertEquals(TipoProduto.SERVICO, produtoNaBase.getTipo());
		assertEquals(0, produtoNaBase.getValorCusto().compareTo(new BigDecimal("5.00")));

	}

	@Test
	public void listProdutos() throws Exception {

		Page<ProdutoDTO> page = produtoService.findAll(null, null, PageRequest.of(0, 1));
		assertEquals(0, page.getContent().size());

		for (int i = 0; i < 10; i++) {
			ProdutoDTO dto = new ProdutoDTO();
			dto.setTipo(TipoProduto.PRODUTO);
			dto.setAtivo(i > 4); // os 5 primeiros essão inativos
			dto.setValorCusto(new BigDecimal("100.00"));
			dto = produtoService.create(dto);
		}

		page = produtoService.findAll(null, null, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(10, page.getTotalElements());
		assertEquals(5, page.getTotalPages());

		page = produtoService.findAll(null, null, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(10, page.getTotalElements());
		assertEquals(5, page.getTotalPages());

		for (int i = 0; i < 10; i++) {
			ProdutoDTO dto = new ProdutoDTO();
			dto.setTipo(TipoProduto.SERVICO);
			dto.setAtivo(i > 4); // os 5 primeiros essão inativos
			dto.setValorCusto(new BigDecimal("100.00"));
			dto = produtoService.create(dto);
		}

		page = produtoService.findAll(null, null, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(20, page.getTotalElements());
		assertEquals(10, page.getTotalPages());

		page = produtoService.findAll(null, true, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(10, page.getTotalElements());
		assertEquals(5, page.getTotalPages());

		page = produtoService.findAll(TipoProduto.SERVICO, null, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(10, page.getTotalElements());
		assertEquals(5, page.getTotalPages());

		page = produtoService.findAll(TipoProduto.PRODUTO, null, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(10, page.getTotalElements());
		assertEquals(5, page.getTotalPages());

		page = produtoService.findAll(TipoProduto.SERVICO, true, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(5, page.getTotalElements());
		assertEquals(3, page.getTotalPages());

		page = produtoService.findAll(TipoProduto.PRODUTO, true, PageRequest.of(0, 2));
		assertEquals(2, page.getContent().size());
		assertEquals(5, page.getTotalElements());
		assertEquals(3, page.getTotalPages());

	}

	@Autowired
	private PedidoService pedidoService;

	@Test
	public void createPedido() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setData(LocalDate.of(2021, 5, 22));
		dto.setSituacao(SituacaoPedido.ABERTO);
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.create(dto);

		assertNotNull(dto.getId());

		PedidoDTO pedidoNaBase = pedidoService.findOne(dto.getId());

		assertEquals(0, pedidoNaBase.getItens().size());
		assertEquals(LocalDate.of(2021, 5, 22), pedidoNaBase.getData());
		assertEquals(SituacaoPedido.ABERTO, pedidoNaBase.getSituacao());
		assertEquals(0, BigDecimal.ZERO.compareTo(pedidoNaBase.getValorTotal()));

	}

	@Test
	public void updatePedido() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setData(LocalDate.of(2010, 10, 10));
		dto.setSituacao(SituacaoPedido.FECHADO);
		dto.setValorTotal(new BigDecimal("10.00"));
		dto = pedidoService.create(dto);

		assertNotNull(dto.getId());

		dto.setData(LocalDate.of(2021, 5, 22));
		dto.setSituacao(SituacaoPedido.ABERTO);
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.update(dto.getId(), dto);

		PedidoDTO pedidoNaBase = pedidoService.findOne(dto.getId());

		assertEquals(0, pedidoNaBase.getItens().size());
		assertEquals(LocalDate.of(2021, 5, 22), pedidoNaBase.getData());
		assertEquals(SituacaoPedido.ABERTO, pedidoNaBase.getSituacao());
		assertEquals(0, BigDecimal.ZERO.compareTo(pedidoNaBase.getValorTotal()));

	}

	@Test
	public void deletePedido() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setData(LocalDate.of(2010, 10, 10));
		dto.setSituacao(SituacaoPedido.FECHADO);
		dto.setValorTotal(new BigDecimal("10.00"));
		dto = pedidoService.create(dto);

		String id = dto.getId();
		assertNotNull(id);

		pedidoService.remove(id);
		NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
			produtoService.findOne(id);
		});

		assertTrue(thrown.getMessage().equals("Não foi encontrado o produto com o id '" + id + "'"));

	}

	@Test
	public void listPedidos() throws Exception {

		Page<PedidoDTO> page = pedidoService.findAll(PageRequest.of(0, 1), null, null, null);
		assertEquals(0, page.getContent().size());

		for (int i = 1; i < 13; i++) {
			PedidoDTO dto = new PedidoDTO();
			dto.setData(LocalDate.of(2021, i, 10)); // gera um pedido por mês do ano 2021
			dto.setSituacao(i < 5 ? SituacaoPedido.FECHADO : SituacaoPedido.ABERTO); // os pedidos dos 4 primeiros meses estão fechados
			dto.setValorTotal(BigDecimal.ZERO);
			dto = pedidoService.create(dto);
		}

		page = pedidoService.findAll(PageRequest.of(0, 2), null, null, null);
		assertEquals(2, page.getContent().size());
		assertEquals(12, page.getTotalElements());
		assertEquals(6, page.getTotalPages());

		page = pedidoService.findAll(PageRequest.of(0, 12), LocalDate.of(2021, 6, 11), null, null);
		assertEquals(6, page.getContent().size());
		assertEquals(6, page.getTotalElements());
		assertEquals(1, page.getTotalPages());

		page = pedidoService.findAll(PageRequest.of(0, 12), null, LocalDate.of(2021, 6, 10), null);
		assertEquals(6, page.getContent().size());
		assertEquals(6, page.getTotalElements());
		assertEquals(1, page.getTotalPages());

		page = pedidoService.findAll(PageRequest.of(0, 12), LocalDate.of(2021, 5, 10), LocalDate.of(2021, 7, 10), null);
		assertEquals(3, page.getContent().size());
		assertEquals(3, page.getTotalElements());
		assertEquals(1, page.getTotalPages());

		page = pedidoService.findAll(PageRequest.of(0, 1), LocalDate.of(2021, 5, 10), LocalDate.of(2021, 7, 10), null);
		assertEquals(1, page.getContent().size());
		assertEquals(3, page.getTotalElements());
		assertEquals(3, page.getTotalPages());

		page = pedidoService.findAll(PageRequest.of(0, 1), null, null, SituacaoPedido.FECHADO);
		assertEquals(1, page.getContent().size());
		assertEquals(4, page.getTotalElements());
		assertEquals(4, page.getTotalPages());

	}

	@Test
	public void adicionarItemPedidoProdutoAtivo() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setSituacao(SituacaoPedido.ABERTO);
		dto.setData(LocalDate.now());
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.create(dto);

		ProdutoDTO produto = new ProdutoDTO();
		produto.setAtivo(true);
		produto.setTipo(TipoProduto.PRODUTO);
		produto.setValorCusto(new BigDecimal("50.00"));
		produto = produtoService.create(produto);

		PedidoItemDTO item = new PedidoItemDTO();
		item.setProduto(produto);

		PedidoDTO pedidoNaBase = pedidoService.adicionarItem(dto.getId(), item);
		assertEquals(1, pedidoNaBase.getItens().size());
		assertEquals(0, pedidoNaBase.getValorTotal().compareTo(new BigDecimal("50.00")));

	}

	@Test
	public void adicionarItemPedidoProdutoInativo() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setSituacao(SituacaoPedido.ABERTO);
		dto.setData(LocalDate.now());
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.create(dto);

		ProdutoDTO produto = new ProdutoDTO();
		produto.setAtivo(false);
		produto.setTipo(TipoProduto.PRODUTO);
		produto.setValorCusto(new BigDecimal("50.00"));
		produto = produtoService.create(produto);

		PedidoItemDTO item = new PedidoItemDTO();
		item.setProduto(produto);

		String id = dto.getId();
		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			pedidoService.adicionarItem(id, item);
		});
		assertTrue(thrown.getMessage().equals("Produtos inativos não podem ser adicionados ao pedido"));
	}

	@Test
	public void removerItemPedido() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setSituacao(SituacaoPedido.ABERTO);
		dto.setData(LocalDate.now());
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.create(dto);

		ProdutoDTO produto = new ProdutoDTO();
		produto.setAtivo(true);
		produto.setTipo(TipoProduto.PRODUTO);
		produto.setValorCusto(new BigDecimal("50.00"));
		produto = produtoService.create(produto);

		PedidoItemDTO item = new PedidoItemDTO();
		item.setProduto(produto);

		PedidoDTO pedidoNaBase = pedidoService.adicionarItem(dto.getId(), item);
		assertEquals(1, pedidoNaBase.getItens().size());

		pedidoNaBase = pedidoService.removerItem(pedidoNaBase.getId(), pedidoNaBase.getItens().get(0));
		assertEquals(0, pedidoNaBase.getItens().size());
	}

	@Test
	public void atribuirDescontoPedido() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setSituacao(SituacaoPedido.ABERTO);
		dto.setData(LocalDate.now());
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.create(dto);

		// produto
		ProdutoDTO produto = new ProdutoDTO();
		produto.setAtivo(true);
		produto.setTipo(TipoProduto.PRODUTO);
		produto.setValorCusto(new BigDecimal("50.00"));
		produto = produtoService.create(produto);

		PedidoItemDTO item = new PedidoItemDTO();
		item.setProduto(produto);

		PedidoDTO pedidoNaBase = pedidoService.adicionarItem(dto.getId(), item);

		// serviço
		produto = new ProdutoDTO();
		produto.setAtivo(true);
		produto.setTipo(TipoProduto.SERVICO);
		produto.setValorCusto(new BigDecimal("50.00"));
		produto = produtoService.create(produto);

		item = new PedidoItemDTO();
		item.setProduto(produto);

		pedidoNaBase = pedidoService.adicionarItem(dto.getId(), item);

		assertEquals(0, pedidoNaBase.getValorTotal().compareTo(new BigDecimal("100.00")));

		PedidoDescontoDTO desconto = new PedidoDescontoDTO();

		desconto.setPercentualDesconto(new BigDecimal("0"));
		pedidoNaBase = pedidoService.atribuirDesconto(pedidoNaBase.getId(), desconto);
		assertEquals(0, pedidoNaBase.getValorTotal().compareTo(new BigDecimal("100")));

		desconto.setPercentualDesconto(new BigDecimal("15"));
		pedidoNaBase = pedidoService.atribuirDesconto(pedidoNaBase.getId(), desconto);
		assertEquals(0, pedidoNaBase.getValorTotal().compareTo(new BigDecimal("92.50")));

		desconto.setPercentualDesconto(new BigDecimal("50"));
		pedidoNaBase = pedidoService.atribuirDesconto(pedidoNaBase.getId(), desconto);
		assertEquals(0, pedidoNaBase.getValorTotal().compareTo(new BigDecimal("75")));

		desconto.setPercentualDesconto(new BigDecimal("100"));
		pedidoNaBase = pedidoService.atribuirDesconto(pedidoNaBase.getId(), desconto);
		assertEquals(0, pedidoNaBase.getValorTotal().compareTo(new BigDecimal("50.00")));

	}

	@Test
	public void atribuirDescontoPedidoFechado() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setSituacao(SituacaoPedido.FECHADO);
		dto.setData(LocalDate.now());
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.create(dto);

		// produto
		ProdutoDTO produto = new ProdutoDTO();
		produto.setAtivo(true);
		produto.setTipo(TipoProduto.PRODUTO);
		produto.setValorCusto(new BigDecimal("50.00"));
		produto = produtoService.create(produto);

		PedidoItemDTO item = new PedidoItemDTO();
		item.setProduto(produto);

		PedidoDTO pedidoNaBase = pedidoService.adicionarItem(dto.getId(), item);

		assertEquals(0, pedidoNaBase.getValorTotal().compareTo(new BigDecimal("50.00")));

		PedidoDescontoDTO desconto = new PedidoDescontoDTO();
		desconto.setPercentualDesconto(new BigDecimal("10"));

		String id = dto.getId();
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			pedidoService.atribuirDesconto(id, desconto);
		});
		assertTrue(thrown.getMessage().equals("Não pode atribuir desconto para um pedido que não está com a situação em aberto"));

	}

	@Test
	public void removerProdutoAssociadoItem() throws Exception {

		PedidoDTO dto = new PedidoDTO();
		dto.setSituacao(SituacaoPedido.ABERTO);
		dto.setData(LocalDate.now());
		dto.setValorTotal(BigDecimal.ZERO);
		dto = pedidoService.create(dto);

		ProdutoDTO produto = new ProdutoDTO();
		produto.setAtivo(true);
		produto.setTipo(TipoProduto.PRODUTO);
		produto.setValorCusto(new BigDecimal("50.00"));
		produto = produtoService.create(produto);

		PedidoItemDTO item = new PedidoItemDTO();
		item.setProduto(produto);

		PedidoDTO pedidoNaBase = pedidoService.adicionarItem(dto.getId(), item);

		String id = pedidoNaBase.getItens().get(0).getProduto().getId();
		produtoService.remove(id);
	}

}
