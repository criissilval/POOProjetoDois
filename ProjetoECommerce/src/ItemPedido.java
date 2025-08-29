import java.util.Objects;

public class ItemPedido {
    private final Produtos produto;
    private int quantidade;
    private double precoVenda;

    public ItemPedido(Produtos produto, int quantidade, double precoVenda) {
        if (produto == null){
            throw new IllegalArgumentException("Produto é obrigatório");
        }
        if (quantidade <= 0){
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (precoVenda <= 0){
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }
    // get e set
    public Produtos getProduto(){
        return produto;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public double getPrecoVenda() {
        return precoVenda;
    }
    public void setQuantidade(int quantidade){
        if (quantidade <= 0){
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.quantidade = quantidade;
    }
    public void setPrecoVenda(double precoVenda){
        if (precoVenda <= 0){
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }
        this.precoVenda = precoVenda;
    }
    public double getSubTotal(){
        return quantidade * precoVenda;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "produtoID=" + produto.getId() +
                ", produtoNome='" + produto.getNome() + '\'' +
                ", quantidade=" + quantidade +
                ", valorTotal=" + precoVenda +
                ", subTotal=" + getSubTotal() +
                '}';
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof ItemPedido)) return false;
        ItemPedido that = (ItemPedido) obj;
        return this.produto.getId() == that.produto.getId();
    }
    @Override
    public int hashCode() {
        return Objects.hash(produto.getId());
    }


}


