public class Produto {
    private int id;
    private String nome;
    private double preco;

    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }
    // get e set
    public int getId() {return id;}

    public void setNome(String nome) {this.nome = nome;}
    public String getNome() {return nome;}

    public void setPreco(double preco) {this.preco = preco;}
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
