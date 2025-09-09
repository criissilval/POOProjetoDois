# Projeto E-Commerce (POO / Java)

Sistema de E-Commerce em console para cadastro e gerenciamento de Clientes, Produtos e Pedidos, desenvolvido aplicando princípios de Programação Orientada a Objetos (POO) e boas práticas (SRP, OCP, DIP), com armazenamento em memória e notificações por e-mail (simuladas em console).

## Principais funcionalidades
- Clientes
  - Cadastrar, listar e atualizar
  - Validações: documento obrigatório (11 dígitos), e-mail válido e normalizado
  - Unicidade: documento único e e-mail único (case-insensitive)
- Produtos
  - Cadastrar, listar e atualizar
  - Unicidade: ID único
- Pedidos
  - Criar pedido para cliente
  - Adicionar/alterar/remover itens enquanto o pedido está ABERTO
  - Preço de venda por item independente do preço do produto
  - Finalizar pedido (valida ao menos 1 item e total > 0) → status de pagamento: AGUARDANDO_PAGAMENTO
  - Pagar pedido (somente quando AGUARDANDO_PAGAMENTO) → pagamento: PAGO, pedido: PAGO
  - Entregar pedido (somente após pagamento) → pedido: FINALIZADO
  - Notificações por e-mail (console) para: aguardando pagamento, pagamento aprovado e pedido entregue

## Menu da aplicação
- Clientes
  - Listar, Cadastrar, Atualizar
- Produtos
  - Listar, Cadastrar, Atualizar
- Pedidos
  - Listar, Criar, Gerenciar (adicionar/remover/alterar itens, finalizar, pagar, entregar)

## Regras de negócio implementadas
- Todo cliente precisa de documento de identificação (11 dígitos)
- Pedido registra data de criação automaticamente
- Pedido inicia com status ABERTO
- Apenas pedidos ABERTO podem receber/alterar/remover itens
- Preço de venda do item pode ser diferente do preço do produto
- Para finalizar: ao menos 1 item e total > 0; altera status do pagamento para AGUARDANDO_PAGAMENTO e notifica cliente
- Pagar: somente quando AGUARDANDO_PAGAMENTO; altera pagamento para PAGO, pedido para PAGO e notifica cliente
- Entregar: somente quando pagamento PAGO; altera pedido para FINALIZADO e notifica cliente

## Validações, Unicidade e Privacidade (LGPD)
- Cliente
  - Documento: obrigatório, somente dígitos (11)
  - E-mail: normalizado (trim + minúsculas) e validado por regex + regras adicionais
  - Unicidade: documento único e e-mail único (case-insensitive) no `RepositorioCliente`
  - Privacidade: documento é mascarado na exibição (3 primeiros + 2 últimos, meio com `*`)
- Produto
  - ID único no `RepositorioProduto`
- Item do pedido
  - Quantidade > 0
  - Preço de venda > 0

## Arquitetura e organização
- Entidades: `Cliente`, `Produto`, `Pedido`, `ItemPedido`
- Catálogos/Repos: `RepositorioBase<T>`, `RepositorioCliente`, `RepositorioProduto`, `RepositorioPedido`, `IRepositorio<T>`
- Domínio de regras: `StatusPedido`, `StatusPagamento`, `Email`
- UI (console): `Main`

Pontos de design:
- Repositórios em memória com herança (`RepositorioBase<T>`) e interface (`IRepositorio<T>`) para polimorfismo
- Encapsulamento e validações nas entidades (ex.: `Cliente#setEmail(...)` valida e normaliza; `Cliente#getDocumentoMascarado()` aplica LGPD)
- `Pedido` controla regras de estado e operações de itens, total e transições com notificações

## Guia passo a passo (exemplos do menu)

### 1) Clientes

- Cadastrar cliente válido
```text
===== ADA TECH - E-COMMERCE =====
1) Clientes
2) Produtos
3) Pedidos
0) Sair
Escolha uma opção: 1

--- Clientes ---
1) Listar
2) Cadastrar
3) Atualizar
0) Voltar
Opção: 2
Nome: Carlos Silva
Documento (11 dígitos): 12345678910
E-mail: CARLOS.SILVA@Email.com
Cliente cadastrado com sucesso!
```
Observação: o e-mail será armazenado normalizado (minúsculas, sem espaços) e o documento será exibido mascarado nas listagens.

- Tentar cadastrar com e-mail já usado
```text
--- Clientes ---
Opção: 2
Nome: Carlos S.
Documento (11 dígitos): 12345678999
E-mail: carlos.silva@email.com
Conflito ao cadastrar cliente: Já existe cliente com e-mail carlos.silva@email.com
```

- Tentar cadastrar com documento inválido (não 11 dígitos)
```text
--- Clientes ---
Opção: 2
Nome: Ana
Documento (11 dígitos): 1234
E-mail: ana@email.com
Dados inválidos ao cadastrar cliente: Documento deve ter exatamente 11 dígitos.
```

- Atualizar e-mail (com verificação de unicidade)
```text
--- Clientes ---
Opção: 3
Documento do cliente a atualizar:
12345678910
Novo nome (enter para manter: Carlos Silva):
Novo e-mail (enter para manter: carlos.silva@email.com):
Conflito ao atualizar cliente: E-mail já utilizado por outro cliente: carlos.silva@email.com
```

- Listar clientes
```text
--- Clientes ---
Opção: 1
Cliente | Nome: Carlos Silva | Documento: 123******10 | E-mail: carlos.silva@email.com
...
```

### 2) Produtos

- Cadastrar produto válido
```text
===== ADA TECH - E-COMMERCE =====
Opção: 2

--- Produtos ---
1) Listar
2) Cadastrar
3) Atualizar
0) Voltar
Opção: 2
ID: 3
Nome: Teclado
Preço: 150
Produto cadastrado.
```

- Tentar cadastrar com ID duplicado
```text
--- Produtos ---
Opção: 2
ID: 3
Nome: Teclado Mecânico
Preço: 300
Conflito ao cadastrar produto: Já existe produto com id 3
```

- Atualizar produto
```text
--- Produtos ---
Opção: 3
ID do produto a atualizar:
3
Novo nome (enter para manter: Teclado):
Teclado USB
Novo preço (enter para manter: 150.0):
180
Produto atualizado.
```

- Listar produtos
```text
--- Produtos ---
Opção: 1
Produto #1 | Nome: Notebook | Preço: R$ 1000,00
Produto #2 | Nome: Mouse | Preço: R$ 80,00
Produto #3 | Nome: Teclado USB | Preço: R$ 180,00
```

### 3) Pedidos

- Criar pedido
```text
===== ADA TECH - E-COMMERCE =====
Opção: 3

--- Pedidos ---
1) Listar pedidos
2) Criar novo pedido
3) Gerenciar pedido existente
0) Voltar
Opção: 2
Digite o documento do cliente:
12345678910
Pedido criado com id: 1
```

- Gerenciar pedido: adicionar item, alterar quantidade, remover, finalizar, pagar, entregar
```text
--- Pedidos ---
Opção: 3
Informe o id do pedido:
1

--- Pedido #1 ---
Cliente: Cliente | Nome: Carlos Silva | Doc: 123******10 | E-mail: carlos.silva@email.com
Status Pedido: ABERTO | Status Pagamento: NENHUM
Itens:
(sem itens)
Total: 0.0

Ações:
1) Adicionar item
2) Remover item
3) Alterar quantidade de item
4) Finalizar (aguardar pagamento)
5) Pagar
6) Entregar
0) Voltar
Opção: 1
ID do produto:
2
Quantidade:
2
Preço de venda (por item):
75
Item adicionado/atualizado.
```

- Tentar finalizar sem itens ou total <= 0
```text
Opção: 4
Erro: Pedido precisa ter ao menos um item
```

- Finalizar (válido)
```text
Opção: 4
Pedido finalizado. Status agora: AGUARDANDO_PAGAMENTO
Email para carlos.silva@email.com:
Seu pedido #1 está aguardando pagamento.
```

- Pagar
```text
Opção: 5
Pagamento aprovado. Status: PAGO
E-mail para carlos.silva@email.com:
Pagamento do pedido #1 aprovado.
```

- Entregar
```text
Opção: 6
Pedido entregue. Status: FINALIZADO
E-mail para carlos.silva@email.com:
Pedido #1 foi entregue. Obrigado!
```

- Tentar alterar itens após sair de ABERTO
```text
Opção: 1
ID do produto:
2
Quantidade:
1
Preço de venda (por item):
70
Erro: Apenas pedidos ABERTO podem ser alterados.
```

### 4) Erros comuns e mensagens esperadas

- Cliente não encontrado (documento mascarado na mensagem)
```text
Digite o documento do cliente:
00000000000
Cliente não encontrado para documento 000******00.
```

- Produto não encontrado
```text
ID do produto:
999
Produto não encontrado.
```

- ID inválido em inputs numéricos
```text
ID:
abc
ID inválido.
```

- Preço/quantidade inválidos
```text
Quantidade:
0
Erro: Quantidade deve ser maior que zero
```

---
Desenvolvido como exercício prático de POO e boas práticas de design em Java.
