# Baozi Store – API REST

Trabalho de Desenvolvimento Web Back-End (UNINTER).
Tecnologias: Java 21, Spring Boot 3, Spring Data JPA, H2 / MySQL, Postman.

## Como executar

Pré-requisitos: JDK 21+ e Maven (ou use o Maven embutido da sua IDE).

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. Por padrão usa **H2** (arquivo `./data/baozidb`), sem instalar nada.
Console do banco: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:file:./data/baozidb`, usuário `sa`, senha vazia).

### Usando MySQL (opcional)

1. Tenha o MySQL rodando e ajuste usuário/senha em `src/main/resources/application-mysql.properties`.
2. Execute: `mvn spring-boot:run -Dspring-boot.run.profiles=mysql`
   (o banco `baozi_store` é criado automaticamente).

## Estrutura

```
src/main/java/com/baozistore
├── BaoziStoreApplication.java
├── model         -> Cliente, Produto, Pedido (entidades JPA)
├── repository    -> ClienteRepository, ProdutoRepository, PedidoRepository
├── controller    -> ClienteController, ProdutoController, PedidoController
├── dto           -> PedidoRequest (clienteId, produtoId, quantidade)
└── exception     -> tratamento de erros (404, 400, 409)
```

## Entidades e campos

| Entidade | Campos |
|---|---|
| Cliente | id (Long), nome (String), clienteDesde (LocalDate) |
| Produto | id (Long), nome (String), preco (BigDecimal), estoque (Boolean) |
| Pedido  | id (Long), cliente_id (FK), produto_id (FK), quantidade (Integer) |

Relacionamentos: Cliente 1—N Pedido; Produto 1—N Pedido.

## Endpoints

Para `/clientes`, `/produtos` e `/pedidos`:

| Método | Rota | Descrição | Status |
|---|---|---|---|
| POST | `/{recurso}` | Criar | 201 |
| GET | `/{recurso}` | Listar todos | 200 |
| GET | `/{recurso}/{id}` | Consultar por ID | 200 / 404 |
| PUT | `/{recurso}/{id}` | Atualizar (opcional) | 200 / 404 |
| DELETE | `/{recurso}/{id}` | Apagar | 204 / 404 |

## Exemplos de JSON (Postman)

Ordem sugerida: Cliente → Produto → Pedido.

**POST /clientes** (troque pelo seu nome + RU)
```json
{ "nome": "SeuNome123456", "clienteDesde": "2026-10-29" }
```

**POST /produtos**
```json
{ "nome": "Baozi de Carne", "preco": 6.50, "estoque": true }
```

**POST /pedidos**
```json
{ "clienteId": 1, "produtoId": 1, "quantidade": 5 }
```

A resposta do pedido já traz os dados completos do cliente e do produto (nome incluído),
o que atende à exigência de que as informações da descrição apareçam nos prints.

## Regras e erros

- Pedido com cliente/produto inexistente → 404.
- Pedido de produto com `estoque: false` → 400.
- Quantidade menor que 1, nome vazio, preço ≤ 0 → 400 com mensagem do campo.
- Apagar cliente/produto que já tem pedido → 409 (apague o pedido antes).

## Coleção do Postman

Importe `postman/BaoziStore.postman_collection.json` no Postman (File > Import).
Ajuste o nome do cliente (nome + RU) antes de executar.
Ordem dos prints: 1 a 12 da coleção (POSTs, GETs gerais, GETs por ID e DELETEs).
