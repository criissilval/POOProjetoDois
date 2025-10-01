public class Produto {
    private int id;
    private String nome;
    private double preco;

    public Produto(int id, String nome, double preco) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.id = id;
        this.nome = nome.trim();
        this.preco = preco;
    }
    // get e set
    public int getId() {return id;}

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }
    public String getNome() {return nome;}

    public void setPreco(double preco) {
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.preco = preco;
    }
    public double getPreco() {return preco;}

    @Override
    public String toString() {
        String s = String.format(
                "%sProduto%s #%d %s|%s Nome: %s %s|%s Preço: %sR$ %.2f%s",
                ConsoleColors.CYAN, ConsoleColors.RESET, id,
                ConsoleColors.DIM, ConsoleColors.RESET, nome,
                ConsoleColors.DIM, ConsoleColors.RESET,
                ConsoleColors.GREEN, preco, ConsoleColors.RESET
        );
        return ConsoleColors.clean(s);
    }
}

