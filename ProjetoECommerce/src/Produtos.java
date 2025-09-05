public class Produtos {
    private int id;
    private String nome;
    private double preco;

    public Produtos(int id, String nome, double preco) {
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
        return String.format("Produto #%d | Nome: %s | Preço: R$ %.2f", id, nome, preco);

    }
}
