import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Pedido {
    private final int id;
    private final Cliente cliente;
    private final LocalDateTime dataCriacao;
    private final StatusPedido statusPedido;
    private final StatusPagamento statusPagamento;
    private final List<ItemPedido> itens = new ArrayList<>();

    public Pedido(int id, Cliente cliente, LocalDateTime dataCriacao, StatusPedido statusPedido, StatusPagamento statusPagamento) {
        if (cliente == null){
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        this.id = id;
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
        this.statusPedido = statusPedido.ABERTO;
        this.statusPagamento = statusPagamento.NENHUM;
    }

    // getters

    public int getId(){
        return id;
    }
    public Cliente getCliente(){
        return cliente;
    }

    public LocalDateTime getDataCriacao(){
        return dataCriacao;
    }
    public StatusPedido getStatusPedido(){
        return statusPedido;
    }
    public StatusPagamento getStatusPagamento(){
        return statusPagamento;
    }
    public List<ItemPedido> getItens(){
        return new ArrayList<>(itens);
    }

    //ação de itens

    public void adicionarItem(Produtos produto, int quantidade, double precoVenda){
        if (produto == null) throw new IllegalArgumentException("Produto é obrigatório");
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        if (precoVenda <= 0) throw new IllegalArgumentException("Valor total deve ser maior que zero");

    }
    public void removerItem(int produtoId){

    }
    public void alterarQuantidadeItem(int produtoId, int novaQuantidade){

    }













}
