import java.util.Objects;

public class ItemPedido {
    private final Produto produto;
    private int quantidade;
    private double precoVenda;

    public ItemPedido(Produto produto, int quantidade, double precoVenda) {
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
    public Produto getProduto(){
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
        String s = String.format(
                "%sItem%s | Produto: %s%s%s (ID %s%d%s) | Qtd: %s%d%s | Preço venda: %sR$ %.2f%s | Subtotal: %sR$ %.2f%s",
                ConsoleColors.BRIGHT_WHITE, ConsoleColors.RESET,
                ConsoleColors.BRIGHT_YELLOW, produto.getNome(), ConsoleColors.RESET,
                ConsoleColors.DIM, produto.getId(), ConsoleColors.RESET,
                ConsoleColors.CYAN, quantidade, ConsoleColors.RESET,
                ConsoleColors.GREEN, precoVenda, ConsoleColors.RESET,
                ConsoleColors.BRIGHT_GREEN, getSubTotal(), ConsoleColors.RESET
        );
        return ConsoleColors.clean(s);
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


