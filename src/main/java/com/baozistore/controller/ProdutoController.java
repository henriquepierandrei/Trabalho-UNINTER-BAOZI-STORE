package com.baozistore.controller;

import com.baozistore.exception.RecursoNaoEncontradoException;
import com.baozistore.model.Produto;
import com.baozistore.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto) {
        produto.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(produto));
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @Valid @RequestBody Produto dados) {
        Produto produto = buscarPorId(id);
        produto.setNome(dados.getNome());
        produto.setPreco(dados.getPreco());
        produto.setEstoque(dados.getEstoque());
        return repository.save(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto", id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
