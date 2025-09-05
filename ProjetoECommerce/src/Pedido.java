import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Pedido {
    private final int id;
    private final Cliente cliente;
    private LocalDateTime dataCriacao;
    private final Email email;
    private StatusPedido statusPedido;
    private StatusPagamento statusPagamento;
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido(int id, Cliente cliente, Email email) {
        if (cliente == null){throw new IllegalArgumentException("Cliente é obrigatório");}
        if (email == null){throw new IllegalArgumentException("Email é obrigatório");}
        this.id = id;
        this.cliente = cliente;
        this.email = email;
        this.dataCriacao = LocalDateTime.now();
        this.statusPedido = statusPedido.ABERTO;
        this.statusPagamento = statusPagamento.NENHUM;
        this.itens = new ArrayList<>();
    }

    // getters

    public int getId(){
        return id;
    }
    public Cliente getCliente(){
        return cliente;
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
        validarEstadoAberto();
        ItemPedido novo = new ItemPedido(produto, quantidade, precoVenda);
        int idx = itens.indexOf(novo);
        if (idx >= 0){
            ItemPedido itemExistente = itens.get(idx);
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            itemExistente.setPrecoVenda(precoVenda);
        }else {
            itens.add(novo);
        }

    }
    public void removerItem(Produtos produto){
        validarEstadoAberto();
        itens.removeIf(ip -> ip.getProduto().getId() == produto.getId());
    }
    public void alterarQuantidadeItem(Produtos produto, int novaQuantidade){
        validarEstadoAberto();
        Optional<ItemPedido> opt = itens.stream()
                .filter(ip -> ip.getProduto().getId() == produto.getId())
                .findFirst();
        if (opt.isEmpty()){
            throw new IllegalArgumentException("Item não encontrado para o produto id=" + produto.getId());
        }
        opt.get().setQuantidade(novaQuantidade);
    }

    public double getTotal(){
        return itens.stream().mapToDouble(ItemPedido::getSubTotal).sum();
    }

    public void finalizar(){
        if (itens.isEmpty()) throw new IllegalArgumentException("Pedido precisa ter ao menos um item");
        if (getTotal() <= 0) throw new IllegalArgumentException("Total do pedido deve ser maior que zero");
        if (statusPedido != StatusPedido.ABERTO){
            throw new IllegalStateException("Apenas pedidos ABERTO porem ser finalizados.");
        }
        statusPedido = StatusPedido.AGUARDANDO_PAGAMENTO;
        statusPagamento = StatusPagamento.AGUARDANDO_PAGAMENTO;
        email.notificarAguardandoPagamento(cliente, this);
    }
    public void pagar(){
        if (statusPedido != StatusPedido.AGUARDANDO_PAGAMENTO){
            throw new IllegalStateException("Pagamento permitido apenas quando o pedido está AGUARDANDO_PAGAMENTO.");
        }
        statusPagamento = StatusPagamento.PAGO;
        statusPedido = StatusPedido.PAGO;
        email.notificarPagamentoAprovado(cliente, this);
    }


    public void entregar(){
        if (statusPagamento != StatusPagamento.PAGO){
            throw new IllegalStateException("Apenas pedidos com pagamento PAGO podem ser entregues.");
        }
        statusPedido = StatusPedido.FINALIZADO;
        email.notificarPedidoEntregue(cliente, this);
    }

    private void validarEstadoAberto(){
        if (statusPedido != StatusPedido.ABERTO){
            throw new IllegalStateException("Apenas pedidos ABERTO podem ser alterados.");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== Pedido #").append(id).append(" =====\n");
        sb.append(String.format("Cliente: %s | Documento: %s | E-mail: %s%n",
                cliente.getNome(), cliente.getDocumento(), cliente.getEmail()));
        sb.append(String.format("Data: %s%n", dataCriacao));
        sb.append(String.format("Status Pedido: %s | Status Pagamento: %s%n",
                statusPedido, statusPagamento));

        sb.append("Itens:\n");
        if (itens.isEmpty()) {
            sb.append("  (sem itens)\n");
        } else {
            for (ItemPedido item : itens) {
                sb.append("  - ").append(item.toString()).append("\n");
            }
        }
        sb.append(String.format("Total: R$ %.2f%n", getTotal()));
        sb.append("=========================\n");
        return sb.toString();
    }

}
