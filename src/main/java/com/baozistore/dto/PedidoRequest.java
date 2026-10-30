package com.baozistore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Corpo do POST/PUT de pedido: usa clienteId e produtoId, como no DER. */
public class PedidoRequest {

    @NotNull(message = "clienteId é obrigatório")
    private Long clienteId;

    @NotNull(message = "produtoId é obrigatório")
    private Long produtoId;

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private Integer quantidade;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
