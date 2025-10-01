
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {

        // Repositório de clientes
        RepositorioCliente clienteRepositorio = new RepositorioCliente();
        clienteRepositorio.cadastrar(new Cliente("Aline", "12345678911", "aline@email.com"));
        clienteRepositorio.cadastrar(new Cliente("Bianca", "12345678912", "bianca@email.com"));

        System.out.println("Clientes cadastrados: ");
        clienteRepositorio.listar().forEach(System.out::println);

        // Repositório de produtos
        RepositorioProduto produtoRepositorio = new RepositorioProduto();
        produtoRepositorio.cadastrar(new Produto(1, "Notebook", 1000.00));
        produtoRepositorio.cadastrar(new Produto(2, "Mouse", 80.00));

        System.out.println("Produtos cadastrados: ");
        produtoRepositorio.listar().forEach(System.out::println);

        // Repositório de cupons
        RepositorioCupom cupomRepositorio = new RepositorioCupom();

        Email email = new Email();
        RepositorioPedido pedidoRepositorio = new RepositorioPedido();

        Scanner sc = new Scanner(System.in);
        int proximoPedidoId = 1;
        while (true) {
            System.out.println("\n===== ADA TECH - E-COMMERCE =====");
            System.out.println("1) Clientes");
            System.out.println("2) Produtos");
            System.out.println("3) Pedidos");
            System.out.println("4) Cupons");
            System.out.println("0) Sair");
            System.out.print("Escolha uma opção: ");
            String op = sc.nextLine().trim();

            switch (op) {
                case "1":
                    menuClientes(sc, clienteRepositorio);
                    break;
                case "2":
                    menuProdutos(sc, produtoRepositorio);
                    break;
                case "3":
                    proximoPedidoId = menuPedidos(sc, clienteRepositorio, produtoRepositorio, pedidoRepositorio, email, proximoPedidoId, cupomRepositorio);
                    break;
                case "4":
                    menuCupons(sc, cupomRepositorio);
                    break;
                case "0":
                    System.out.println("Saindo... Até mais!");
                    sc.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static String maskDoc(String documento) {
        if (documento == null || documento.isBlank()) return "***";
        String digits = documento.replaceAll("\\D", "");
        if (digits.length() < 5) return "***";
        String inicio = digits.substring(0, 3);
        String fim = digits.substring(digits.length() - 2);
        String meio = "*".repeat(digits.length() - 5);
        return inicio + meio + fim;
    }

    private static void menuClientes(Scanner sc, RepositorioCliente clienteRepositorio) throws IllegalAccessException {
        while (true) {
            System.out.println("\n--- Clientes ---");
            System.out.println("1) Listar");
            System.out.println("2) Cadastrar");
            System.out.println("3) Atualizar");
            System.out.println("0) Voltar");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();

            switch (op) {
                case "1":
                    List<Cliente> clientes = clienteRepositorio.listar();
                    if (clientes.isEmpty()) {
                        System.out.println("Nenhum cliente cadastrado.");
                    } else {
                        clientes.forEach(System.out::println);
                    }
                    break;
                case "2":
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Documento (11 dígitos): ");
                    String doc = sc.nextLine();
                    System.out.print("E-mail: ");
                    String email = sc.nextLine();
                    try {
                        Cliente novo = new Cliente(nome, doc, email);
                        clienteRepositorio.cadastrar(novo);
                        System.out.println("Cliente cadastrado com sucesso!");
                    } catch (IllegalStateException dup) {
                        System.out.println("Conflito ao cadastrar cliente: " + dup.getMessage());
                    } catch (IllegalArgumentException inv) {
                        System.out.println("Dados inválidos ao cadastrar cliente: " + inv.getMessage());
                    } catch (Exception e) {
                        System.out.println("Erro inesperado ao cadastrar cliente.");
                    }
                    break;
                case "3":
                    System.out.println("Documento do cliente a atualizar: ");
                    String docAt = sc.nextLine();
                    Cliente existente = clienteRepositorio.listar().stream()
                            .filter(c -> c.getDocumento().equals(docAt))
                            .findFirst()
                            .orElse(null);
                    if (existente == null) {
                        System.out.println("Cliente não encontrado para documento " + maskDoc(docAt) + ".");
                        break;
                    }
                    System.out.print("Novo nome (enter para manter: " + existente.getNome() + "): ");
                    String novoNome = sc.nextLine();
                    if (!novoNome.isBlank()) existente.setNome(novoNome);

                    System.out.print("Novo e-mail (enter para manter: " + existente.getEmail() + "): ");
                    String novoEmail = sc.nextLine();
                    if (!novoEmail.isBlank()) existente.setEmail(novoEmail);

                    try {
                        clienteRepositorio.atualizar(existente);
                        System.out.println("Cliente atualizado com sucesso.");
                    } catch (IllegalStateException dup) {
                        System.out.println("Conflito ao atualizar cliente: " + dup.getMessage());
                    } catch (IllegalArgumentException inv) {
                        System.out.println("Dados inválidos ao atualizar cliente: " + inv.getMessage());
                    } catch (Exception e) {
                        System.out.println("Erro inesperado ao atualizar cliente.");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuProdutos(Scanner sc, RepositorioProduto produtoRepositorio) {
        while (true) {
            System.out.println("\n--- Produtos ---");
            System.out.println("1) Listar");
            System.out.println("2) Cadastrar");
            System.out.println("3) Atualizar");
            System.out.println("0) Voltar");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();

            switch (op) {
                case "1":
                    List<Produto> produtos = produtoRepositorio.listar();
                    if (produtos.isEmpty()) {
                        System.out.println("Nenhum produto cadastrado.");
                    } else {
                        produtos.forEach(System.out::println);
                    }
                    break;
                case "2":
                    try {
                        System.out.print("ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Preço: ");
                        double preco = Double.parseDouble(sc.nextLine());
                        Produto p = new Produto(id, nome, preco);
                        produtoRepositorio.cadastrar(p);
                        System.out.println("Produto cadastrado.");
                    } catch (IllegalStateException dup) {
                        System.out.println("Conflito ao cadastrar produto: " + dup.getMessage());
                    } catch (IllegalArgumentException inv) {
                        System.out.println("Dados inválidos ao cadastrar produto: " + inv.getMessage());
                    } catch (Exception e) {
                        System.out.println("Erro inesperado ao cadastrar produto.");
                    }
                    break;
                case "3":
                    try {
                        System.out.print("ID do produto a atualizar: ");
                        int idAt = Integer.parseInt(sc.nextLine());
                        Produto existente = produtoRepositorio.listar().stream()
                                .filter(p -> p.getId() == idAt)
                                .findFirst()
                                .orElse(null);
                        if (existente == null) {
                            System.out.println("Produto não encontrado.");
                            break;
                        }
                        System.out.print("Novo nome (enter para manter: " + existente.getNome() + "): ");
                        String novoNome = sc.nextLine();
                        if (!novoNome.isBlank()) existente.setNome(novoNome);

                        System.out.print("Novo preço (enter para manter: " + existente.getPreco() + "): ");
                        String precoStr = sc.nextLine();
                        if (!precoStr.isBlank()) {
                            double novoPreco = Double.parseDouble(precoStr);
                            existente.setPreco(novoPreco);
                        }
                        produtoRepositorio.atualizar(existente);
                        System.out.println("Produto atualizado.");
                    } catch (IllegalStateException dup) {
                        System.out.println("Conflito ao atualizar produto: " + dup.getMessage());
                    } catch (IllegalArgumentException inv) {
                        System.out.println("Dados inválidos ao atualizar produto: " + inv.getMessage());
                    } catch (Exception e) {
                        System.out.println("Erro inesperado ao atualizar produto.");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static int menuPedidos(
            Scanner sc,
            RepositorioCliente clienteRepositorio,
            RepositorioProduto produtoRepositorio,
            RepositorioPedido pedidoRepositorio,
            Email email,
            int proximoPedidoId,
            RepositorioCupom cupomRepositorio
    ) {
        while (true) {
            System.out.println("\n--- Pedidos ---");
            System.out.println("1) Listar pedidos");
            System.out.println("2) Criar novo pedido");
            System.out.println("3) Gerenciar pedido existente");
            System.out.println("0) Voltar");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    List<Pedido> pedidos = pedidoRepositorio.listar();
                    if (pedidos.isEmpty()) {
                        System.out.println("Nenhum pedido cadastrado.");
                    } else {
                        pedidos.forEach(System.out::println);
                    }
                    break;
                case "2":
                    Cliente cliente = selecionarClientePorDocumento(sc, clienteRepositorio);
                    if (cliente == null) break;
                    Pedido novo = new Pedido(proximoPedidoId, cliente, email);
                    pedidoRepositorio.cadastrar(novo);
                    System.out.println("Pedido criado com id: " + proximoPedidoId);
                    proximoPedidoId++;
                    break;
                case "3":
                    System.out.print("Informe o id do pedido: ");
                    String idStr = sc.nextLine();
                    int id;
                    try {
                        id = Integer.parseInt(idStr);
                    } catch (Exception e) {
                        System.out.println("ID inválido.");
                        break;
                    }
                    Optional<Pedido> optPedido = pedidoRepositorio.buscarPorId(id);
                    if (optPedido.isEmpty()) {
                        System.out.println("Pedido não encontrado.");
                        break;
                    }
                    gerenciarPedido(sc, optPedido.get(), produtoRepositorio, cupomRepositorio);
                    break;
                case "0":
                    return proximoPedidoId;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuCupons(Scanner sc, RepositorioCupom cupomRepositorio) {
        while (true) {
            System.out.println("\n=== GERENCIAMENTO DE CUPONS ===");
            System.out.println("1) Criar cupom");
            System.out.println("2) Listar cupons");
            System.out.println("3) Listar cupons válidos");
            System.out.println("4) Atualizar cupom");
            System.out.println("5) Expirar cupom");
            System.out.println("0) Voltar");
            System.out.print("Escolha: ");

            String opcaoStr = sc.nextLine().trim();
            int opcao;
            try {
                opcao = Integer.parseInt(opcaoStr);
            } catch (Exception e) {
                System.out.println("Opção inválida!");
                continue;
            }

            switch (opcao) {
                case 1:
                    criarCupom(sc, cupomRepositorio);
                    break;
                case 2:
                    listarCupons(cupomRepositorio);
                    break;
                case 3:
                    listarCuponsValidos(cupomRepositorio);
                    break;
                case 4:
                    atualizarCupom(sc, cupomRepositorio);
                    break;
                case 5:
                    expirarCupom(sc, cupomRepositorio);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void criarCupom(Scanner sc, RepositorioCupom cupomRepositorio) {
        try {
            System.out.print("Código do cupom: ");
            String codigo = sc.nextLine().toUpperCase();

            System.out.print("Tipo de desconto (1-Fixo / 2-Percentual): ");
            int tipoOp = Integer.parseInt(sc.nextLine());
            TipoDesconto tipo = tipoOp == 1 ? TipoDesconto.FIXO : TipoDesconto.PERCENTUAL;

            System.out.print("Valor do desconto: ");
            double valor = Double.parseDouble(sc.nextLine());

            System.out.print("Data de expiração (AAAA-MM-DD): ");
            String dataStr = sc.nextLine();
            LocalDate dataExpiracao = LocalDate.parse(dataStr);

            CupomDesconto cupom = new CupomDesconto(codigo, valor, tipo, dataExpiracao);
            cupomRepositorio.cadastrar(cupom);
            System.out.println("✓ Cupom criado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro ao criar cupom: " + e.getMessage());
        }
    }

    private static void listarCupons(RepositorioCupom cupomRepositorio) {
        List<CupomDesconto> cupons = cupomRepositorio.listar();
        if (cupons.isEmpty()) {
            System.out.println("Nenhum cupom cadastrado.");
        } else {
            System.out.println("\n=== LISTA DE CUPONS ===");
            cupons.forEach(System.out::println);
        }
    }

    private static void listarCuponsValidos(RepositorioCupom cupomRepositorio) {
        List<CupomDesconto> cupons = cupomRepositorio.listarValidos();
        if (cupons.isEmpty()) {
            System.out.println("Nenhum cupom válido disponível.");
        } else {
            System.out.println("\n=== CUPONS VÁLIDOS ===");
            cupons.forEach(System.out::println);
        }
    }

    private static void atualizarCupom(Scanner sc, RepositorioCupom cupomRepositorio) {
        try {
            System.out.print("Código do cupom a atualizar: ");
            String codigo = sc.nextLine().toUpperCase();

            Optional<CupomDesconto> optCupom = cupomRepositorio.buscarPorCodigo(codigo);
            if (optCupom.isEmpty()) {
                System.out.println("Cupom não encontrado.");
                return;
            }

            CupomDesconto cupom = optCupom.get();

            System.out.print("Novo valor de desconto (Enter para manter " + cupom.getValorDesconto() + "): ");
            String valorStr = sc.nextLine();
            if (!valorStr.isBlank()) {
                cupom.setValorDesconto(Double.parseDouble(valorStr));
            }

            System.out.print("Nova data de expiração (AAAA-MM-DD, Enter para manter): ");
            String dataStr = sc.nextLine();
            if (!dataStr.isBlank()) {
                cupom.setDataExpiracao(LocalDate.parse(dataStr));
            }

            cupomRepositorio.atualizar(cupom);
            System.out.println("✓ Cupom atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro ao atualizar cupom: " + e.getMessage());
        }
    }

    private static void expirarCupom(Scanner sc, RepositorioCupom cupomRepositorio) {
        try {
            System.out.print("Código do cupom a expirar: ");
            String codigo = sc.nextLine().toUpperCase();

            Optional<CupomDesconto> optCupom = cupomRepositorio.buscarPorCodigo(codigo);
            if (optCupom.isEmpty()) {
                System.out.println("Cupom não encontrado.");
                return;
            }

            CupomDesconto cupom = optCupom.get();
            cupom.setAtivo(false);
            cupomRepositorio.atualizar(cupom);
            System.out.println("✓ Cupom expirado com sucesso!");
        } catch (Exception e) {
            System.out.println("✗ Erro ao expirar cupom: " + e.getMessage());
        }
    }

    private static Cliente selecionarClientePorDocumento(Scanner sc, RepositorioCliente clienteRepositorio) {
        System.out.println("Digite o documento do cliente: ");
        String doc = sc.nextLine();
        return clienteRepositorio.listar().stream()
                .filter(c -> c.getDocumento().equals(doc))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Cliente não encontrado para documento " + maskDoc(doc) + ".");
                    return null;
                });
    }

    private static Produto selecionarProdutoPorId(Scanner sc, RepositorioProduto produtoRepositorio) {
        System.out.print("ID do produto: ");
        String idStr = sc.nextLine();
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            System.out.println("ID inválido.");
            return null;
        }
        return produtoRepositorio.listar().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Produto não encontrado.");
                    return null;
                });
    }

    private static void gerenciarPedido(Scanner sc, Pedido pedido, RepositorioProduto produtoRepositorio, RepositorioCupom cupomRepositorio) {
        while (true) {
            System.out.println("\n--- Pedido #" + pedido.getId() + " ---");
            System.out.println("Cliente: " + pedido.getCliente());
            System.out.println("Status Pedido: " + pedido.getStatusPedido() + " | Status Pagamento: " + pedido.getStatusPagamento());
            System.out.println("Itens:");
            if (pedido.getItens().isEmpty()) System.out.println("(sem itens)");
            else pedido.getItens().forEach(System.out::println);

            if (pedido.getCupomAplicado() != null) {
                System.out.println("Cupom aplicado: " + pedido.getCupomAplicado().getCodigo());
                System.out.println("Desconto: R$ " + String.format("%.2f", pedido.getValorDesconto()));
            }

            System.out.println("Total: " + pedido.getTotal());

            System.out.println("\nAções:");
            System.out.println("1) Adicionar item");
            System.out.println("2) Remover item");
            System.out.println("3) Alterar quantidade de item");
            System.out.println("4) Aplicar cupom de desconto");
            System.out.println("5) Finalizar (aguardar pagamento)");
            System.out.println("6) Pagar");
            System.out.println("7) Entregar");
            System.out.println("0) Voltar");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();

            try {
                switch (op) {
                    case "1": {
                        Produto prod = selecionarProdutoPorId(sc, produtoRepositorio);
                        if (prod == null) break;
                        System.out.print("Quantidade: ");
                        int qtd = Integer.parseInt(sc.nextLine());
                        System.out.print("Preço de venda (por item): ");
                        double precoVenda = Double.parseDouble(sc.nextLine());
                        pedido.adicionarItem(prod, qtd, precoVenda);
                        System.out.println("Item adicionado/atualizado.");
                        break;
                    }
                    case "2": {
                        Produto prod = selecionarProdutoPorId(sc, produtoRepositorio);
                        if (prod == null) break;
                        pedido.removerItem(prod);
                        System.out.println("Item removido.");
                        break;
                    }
                    case "3": {
                        Produto prod = selecionarProdutoPorId(sc, produtoRepositorio);
                        if (prod == null) break;
                        System.out.print("Nova quantidade: ");
                        int qtd = Integer.parseInt(sc.nextLine());
                        pedido.alterarQuantidadeItem(prod, qtd);
                        System.out.println("Quantidade alterada.");
                        break;
                    }
                    case "4": {
                        System.out.print("Código do cupom: ");
                        String codigo = sc.nextLine().toUpperCase();
                        Optional<CupomDesconto> optCupom = cupomRepositorio.buscarPorCodigo(codigo);
                        if (optCupom.isEmpty()) {
                            System.out.println("✗ Cupom não encontrado.");
                            break;
                        }
                        CupomDesconto cupom = optCupom.get();
                        pedido.aplicarCupom(cupom);
                        System.out.println("✓ Cupom aplicado com sucesso!");
                        break;
                    }
                    case "5":
                        pedido.finalizar();
                        System.out.println("Pedido finalizado. Status agora: " + pedido.getStatusPedido());
                        break;
                    case "6":
                        pedido.pagar();
                        System.out.println("Pagamento aprovado. Status: " + pedido.getStatusPagamento());
                        break;
                    case "7":
                        pedido.entregar();
                        System.out.println("Pedido entregue. Status: " + pedido.getStatusPedido());
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}