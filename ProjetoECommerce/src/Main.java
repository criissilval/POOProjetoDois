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
        produtoRepositorio.cadastrar(new Produtos(1, "Notebook", 1000.00));
        produtoRepositorio.cadastrar(new Produtos(2, "Mouse", 80.00));

        System.out.println("Produtos cadastrados: ");
        produtoRepositorio.listar().forEach(System.out::println);

        Email email = new Email();
        RepositorioPedido pedidoRepositorio = new RepositorioPedido();

        Scanner sc = new Scanner(System.in);
        int proximoPedidoId = 1;
        while (true) {
            System.out.println("\n===== ADA TECH - E-COMMERCE =====");
            System.out.println("1) Clientes");
            System.out.println("2) Produtos");
            System.out.println("3) Pedidos");
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
                    proximoPedidoId = menuPedidos(sc, clienteRepositorio, produtoRepositorio,pedidoRepositorio, email, proximoPedidoId);
                    break;
                case "0":
                    System.out.println("Saindo... Até mais!");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    private static void menuClientes(Scanner sc, RepositorioCliente clienteRepositorio)throws IllegalAccessException {
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
                    } catch (Exception e) {
                        System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
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
                        System.out.println("Cliente não encontrado.");
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
                    }  catch (Exception e) {
                        System.out.println("Erro ao atualizar cliente: " + e.getMessage());
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
                    List<Produtos> produtos = produtoRepositorio.listar();
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
                        Produtos p = new Produtos(id, nome, preco);
                        produtoRepositorio.cadastrar(p);
                        System.out.println("Produto cadastrado.");
                    } catch (Exception e) {
                        System.out.println("Erro ao cadastrar produto: " + e.getMessage());
                    }
                    break;
                case "3":
                    try {
                        System.out.print("ID do produto a atualizar: ");
                        int idAt = Integer.parseInt(sc.nextLine());
                        Produtos existente = produtoRepositorio.listar().stream()
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
                    } catch (Exception e) {
                        System.out.println("Erro ao atualizar produto: " + e.getMessage());
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
            int proximoPedidoId
    ){
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
                    gerenciarPedido(sc, optPedido.get(), produtoRepositorio);
                    break;
                case "0":
                    return proximoPedidoId;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
    private static Cliente selecionarClientePorDocumento(Scanner sc, RepositorioCliente clienteRepositorio) {
        System.out.println("Digite o documento do cliente: ");
        String doc = sc.nextLine();
        return clienteRepositorio.listar().stream()
                .filter(c -> c.getDocumento().equals(doc))
                .findFirst()
                .orElseGet(()->{
                    System.out.println("Cliente não encontrado.");
                    return null;
                });
    }
    private static Produtos selecionarProdutoPorId(Scanner sc, RepositorioProduto produtoRepositorio) {
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

    private static void gerenciarPedido(Scanner sc, Pedido pedido, RepositorioProduto produtoRepositorio) {
        while (true) {
            System.out.println("\n--- Pedido #" + pedido.getId() + " ---");
            System.out.println("Cliente: " + pedido.getCliente());
            System.out.println("Status Pedido: " + pedido.getStatusPedido() + " | Status Pagamento: " + pedido.getStatusPagamento());
            System.out.println("Itens:");
            if (pedido.getItens().isEmpty()) System.out.println("(sem itens)");
            else pedido.getItens().forEach(System.out::println);
            System.out.println("Total: " + pedido.getTotal());

            System.out.println("\nAções:");
            System.out.println("1) Adicionar item");
            System.out.println("2) Remover item");
            System.out.println("3) Alterar quantidade de item");
            System.out.println("4) Finalizar (aguardar pagamento)");
            System.out.println("5) Pagar");
            System.out.println("6) Entregar");
            System.out.println("0) Voltar");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();

            try {
                switch (op) {
                    case "1": {
                        Produtos prod = selecionarProdutoPorId(sc, produtoRepositorio);
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
                        Produtos prod = selecionarProdutoPorId(sc, produtoRepositorio);
                        if (prod == null) break;
                        pedido.removerItem(prod);
                        System.out.println("Item removido.");
                        break;
                    }
                    case "3": {
                        Produtos prod = selecionarProdutoPorId(sc, produtoRepositorio);
                        if (prod == null) break;
                        System.out.print("Nova quantidade: ");
                        int qtd = Integer.parseInt(sc.nextLine());
                        pedido.alterarQuantidadeItem(prod, qtd);
                        System.out.println("Quantidade alterada.");
                        break;
                    }
                    case "4":
                        pedido.finalizar();
                        System.out.println("Pedido finalizado. Status agora: " + pedido.getStatusPedido());
                        break;
                    case "5":
                        pedido.pagar();
                        System.out.println("Pagamento aprovado. Status: " + pedido.getStatusPagamento());
                        break;
                    case "6":
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
