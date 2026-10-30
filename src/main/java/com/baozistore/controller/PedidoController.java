package com.baozistore.controller;

import com.baozistore.dto.PedidoRequest;
import com.baozistore.exception.RecursoNaoEncontradoException;
import com.baozistore.exception.RegraDeNegocioException;
import com.baozistore.model.Cliente;
import com.baozistore.model.Pedido;
import com.baozistore.model.Produto;
import com.baozistore.repository.ClienteRepository;
import com.baozistore.repository.PedidoRepository;
import com.baozistore.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody PedidoRequest request) {
        Pedido pedido = new Pedido();
        preencher(pedido, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRepository.save(pedido));
    }

    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido", id));
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable Long id, @Valid @RequestBody PedidoRequest request) {
        Pedido pedido = buscarPorId(id);
        preencher(pedido, request);
        return pedidoRepository.save(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Pedido", id);
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void preencher(Pedido pedido, PedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", request.getClienteId()));
        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", request.getProdutoId()));

        if (!Boolean.TRUE.equals(produto.getEstoque())) {
            throw new RegraDeNegocioException("O produto \"" + produto.getNome() + "\" está sem estoque");
        }

        pedido.setCliente(cliente);
        pedido.setProduto(produto);
        pedido.setQuantidade(request.getQuantidade());
    }
}2
