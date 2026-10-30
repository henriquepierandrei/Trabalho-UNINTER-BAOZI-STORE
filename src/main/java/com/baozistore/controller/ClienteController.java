package com.baozistore.controller;

import com.baozistore.exception.RecursoNaoEncontradoException;
import com.baozistore.model.Cliente;
import com.baozistore.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        cliente.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(cliente));
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
    }

    @GetMapping
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody Cliente dados) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dados.getNome());
        if (dados.getClienteDesde() != null) {
            cliente.setClienteDesde(dados.getClienteDesde());
        }
        return repository.save(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Cliente", id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
