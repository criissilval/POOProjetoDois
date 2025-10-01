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
  - Aplicar cupons de desconto (somente em pedidos ABERTO)
  - Finalizar pedido (valida ao menos 1 item e total > 0) → status de pagamento: AGUARDANDO_PAGAMENTO
  - Pagar pedido (somente quando AGUARDANDO_PAGAMENTO) → pagamento: PAGO, pedido: PAGO
  - Entregar pedido (somente após pagamento) → pedido: FINALIZADO
  - Notificações por e-mail (console) para: aguardando pagamento, pagamento aprovado e pedido entregue
- Cupons de Desconto
  - Criar cupons com código único, valor, tipo (FIXO/PERCENTUAL) e data de expiração
  - Listar todos os cupons e apenas os válidos
  - Atualizar valor e data de expiração
  - Expirar cupons manualmente
  - Validação automática de validade (ativo, não utilizado, não expirado)
  - Aplicação em pedidos com cálculo automático do desconto

## Menu da aplicação
- Clientes
  - Listar, Cadastrar, Atualizar
- Produtos
  - Listar, Cadastrar, Atualizar
- Pedidos
  - Listar, Criar, Gerenciar (adicionar/remover/alterar itens, aplicar cupom, finalizar, pagar, entregar)
- Cupons
  - Criar, Listar, Listar válidos, Atualizar, Expirar

## Regras de negócio implementadas
- Todo cliente precisa de documento de identificação (11 dígitos)
- Pedido registra data de criação automaticamente
- Pedido inicia com status ABERTO
- Apenas pedidos ABERTO podem receber/alterar/remover itens
- Preço de venda do item pode ser diferente do preço do produto
- Para finalizar: ao menos 1 item e total > 0; altera status do pagamento para AGUARDANDO_PAGAMENTO e notifica cliente
- Pagar: somente quando AGUARDANDO_PAGAMENTO; altera pagamento para PAGO, pedido para PAGO e notifica cliente
- Entregar: somente quando pagamento PAGO; altera pedido para FINALIZADO e notifica cliente
- **Cupons de Desconto:**
  - Código do cupom é único e case-insensitive
  - Valor de desconto deve ser maior que zero
  - Desconto percentual não pode ser maior que 100%
  - Data de expiração é obrigatória
  - Cupom é válido quando: ativo, não utilizado e não expirado
  - Desconto fixo: aplicado até o valor total do pedido
  - Desconto percentual: calculado sobre o valor total
  - Cupons só podem ser aplicados em pedidos ABERTO
  - Cupons podem ser expirados manualmente

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
- Entidades: `Cliente`, `Produto`, `Pedido`, `ItemPedido`, `CupomDesconto`
- Catálogos/Repos: `RepositorioBase<T>`, `RepositorioCliente`, `RepositorioProduto`, `RepositorioPedido`, `RepositorioCupom`, `IRepositorio<T>`
- Domínio de regras: `StatusPedido`, `StatusPagamento`, `TipoDesconto`, `Email`
- UI (console): `Main`

Pontos de design:
- Repositórios em memória com herança (`RepositorioBase<T>`) e interface (`IRepositorio<T>`) para polimorfismo
- Encapsulamento e validações nas entidades (ex.: `Cliente#setEmail(...)` valida e normaliza; `Cliente#getDocumentoMascarado()` aplica LGPD)
- `Pedido` controla regras de estado e operações de itens, total e transições com notificações
- `CupomDesconto` implementa validação automática de validade e cálculo de desconto
- `RepositorioCupom` estende `RepositorioBase` com métodos específicos para busca por código e listagem de cupons válidos

## Guia passo a passo (exemplos do menu)

### 1) Clientes

- Cadastrar cliente válido
```text
===== ADA TECH - E-COMMERCE =====
1) Clientes
2) Produtos
3) Pedidos
4) Cupons
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
1) Clientes
2) Produtos
3) Pedidos
4) Cupons
0) Sair
Escolha uma opção: 2

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

### 3) Cupons de Desconto

- Criar cupom com desconto fixo
```text
===== ADA TECH - E-COMMERCE =====
1) Clientes
2) Produtos
3) Pedidos
4) Cupons
0) Sair
Escolha uma opção: 4

=== GERENCIAMENTO DE CUPONS ===
1) Criar cupom
2) Listar cupons
3) Listar cupons válidos
4) Atualizar cupom
5) Expirar cupom
0) Voltar
Escolha: 1
Código do cupom: DESCONTO10
Tipo de desconto (1-Fixo / 2-Percentual): 1
Valor do desconto: 50
Data de expiração (AAAA-MM-DD): 2024-12-31
✓ Cupom criado com sucesso!
```

- Criar cupom com desconto percentual
```text
=== GERENCIAMENTO DE CUPONS ===
Escolha: 1
Código do cupom: BLACKFRIDAY
Tipo de desconto (1-Fixo / 2-Percentual): 2
Valor do desconto: 15
Data de expiração (AAAA-MM-DD): 2024-12-31
✓ Cupom criado com sucesso!
```

- Listar cupons válidos
```text
=== GERENCIAMENTO DE CUPONS ===
Escolha: 3

=== CUPONS VÁLIDOS ===
Cupom: DESCONTO10 | Desconto: R$ 50.0 | Expira: 2024-12-31 | Status: ✓ VÁLIDO
Cupom: BLACKFRIDAY | Desconto: 15.0% | Expira: 2024-12-31 | Status: ✓ VÁLIDO
```

- Atualizar cupom
```text
=== GERENCIAMENTO DE CUPONS ===
Escolha: 4
Código do cupom a atualizar: DESCONTO10
Novo valor de desconto (Enter para manter 50.0): 75
Nova data de expiração (AAAA-MM-DD, Enter para manter): 2024-11-30
✓ Cupom atualizado com sucesso!
```

- Expirar cupom
```text
=== GERENCIAMENTO DE CUPONS ===
Escolha: 5
Código do cupom a expirar: BLACKFRIDAY
✓ Cupom expirado com sucesso!
```

### 4) Pedidos

- Criar pedido
```text
===== ADA TECH - E-COMMERCE =====
1) Clientes
2) Produtos
3) Pedidos
4) Cupons
0) Sair
Escolha uma opção: 3

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

- Gerenciar pedido: adicionar item, alterar quantidade, remover, aplicar cupom, finalizar, pagar, entregar
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
4) Aplicar cupom de desconto
5) Finalizar (aguardar pagamento)
6) Pagar
7) Entregar
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

- Aplicar cupom de desconto
```text
Opção: 4
Código do cupom: DESCONTO10
✓ Cupom aplicado com sucesso!
```

- Visualizar pedido com cupom aplicado
```text
--- Pedido #1 ---
Cliente: Cliente | Nome: Carlos Silva | Doc: 123******10 | E-mail: carlos.silva@email.com
Status Pedido: ABERTO | Status Pagamento: NENHUM
Itens:
  - ItemPedido{produto=Produto{id=2, nome='Mouse', preco=80.0}, quantidade=2, precoVenda=75.0, subTotal=150.0}
Cupom aplicado: DESCONTO10
Desconto: R$ 50.00
Total: 100.0
```

- Tentar finalizar sem itens ou total <= 0
```text
Opção: 4
Erro: Pedido precisa ter ao menos um item
```

- Finalizar (válido)
```text
Opção: 5
Pedido finalizado. Status agora: AGUARDANDO_PAGAMENTO
Email para carlos.silva@email.com:
Seu pedido #1 está aguardando pagamento.
```

- Pagar
```text
Opção: 6
Pagamento aprovado. Status: PAGO
E-mail para carlos.silva@email.com:
Pagamento do pedido #1 aprovado.
```

- Entregar
```text
Opção: 7
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

### 5) Erros comuns e mensagens esperadas

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

- Cupom não encontrado
```text
Opção: 4
Código do cupom: INEXISTENTE
✗ Cupom não encontrado.
```

- Cupom inválido ou expirado
```text
Opção: 4
Código do cupom: EXPIRADO
Erro: Cupom inválido, expirado ou já utilizado
```

- Tentar aplicar cupom em pedido não aberto
```text
Opção: 4
Código do cupom: DESCONTO10
Erro: Só é possível aplicar cupom em pedidos abertos
```

---
Desenvolvido como exercício prático de POO e boas práticas de design em Java.
