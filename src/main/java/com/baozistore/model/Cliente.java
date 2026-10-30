package com.baozistore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Column(nullable = false)
    private String nome;

    // Se não for informado no POST, assume a data de hoje
    @Column(name = "cliente_desde", nullable = false)
    private LocalDate clienteDesde;

    public Cliente() {
    }

    public Cliente(Long id, String nome, LocalDate clienteDesde) {
        this.id = id;
        this.nome = nome;
        this.clienteDesde = clienteDesde;
    }

    @PrePersist
    void definirDataPadrao() {
        if (clienteDesde == null) {
            clienteDesde = LocalDate.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getClienteDesde() { return clienteDesde; }
    public void setClienteDesde(LocalDate clienteDesde) { this.clienteDesde = clienteDesde; }
}
