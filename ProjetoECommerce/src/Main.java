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


        }
    }
