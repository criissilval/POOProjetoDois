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
    private CupomDesconto cupomAplicado;
    private List<RegraDesconto> regrasDesconto;
    private double valorDesconto;


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
        this.cupomAplicado = null;
        this.regrasDesconto = new ArrayList<>();
        this.valorDesconto = 0.0;

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
    public double getValorTotal() {
        double valorBase = itens.stream()
                .mapToDouble(i -> i.getPrecoVenda() * i.getQuantidade())
                .sum();
        return Math.max(0, valorBase - valorDesconto);
    }

    public CupomDesconto getCupomAplicado() { return cupomAplicado; }
    public double getValorDesconto() { return valorDesconto; }



    //ação de itens

    public void adicionarItem(Produto produto, int quantidade, double precoVenda){
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
    public void removerItem(Produto produto){
        validarEstadoAberto();
        itens.removeIf(ip -> ip.getProduto().getId() == produto.getId());
    }
    public void alterarQuantidadeItem(Produto produto, int novaQuantidade){
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
        sb.append(String.format("%s===== Pedido #%d =====%s%n",
                ConsoleColors.BRIGHT_BLUE, id, ConsoleColors.RESET));

        sb.append(String.format("%sCliente:%s %s | %sDoc:%s %s | %sE-mail:%s %s%n",
                ConsoleColors.CYAN, ConsoleColors.RESET,
                cliente.getNome(),
                ConsoleColors.DIM, ConsoleColors.RESET, cliente.getDocumentoMascarado(),
                ConsoleColors.DIM, ConsoleColors.RESET, cliente.getEmail()
        ));

        sb.append(String.format("%sData:%s %s%n",
                ConsoleColors.CYAN, ConsoleColors.RESET, dataCriacao));

        sb.append(String.format("%sStatus Pedido:%s %s | %sStatus Pagamento:%s %s%n",
                ConsoleColors.CYAN, ConsoleColors.RESET, statusPedido,
                ConsoleColors.CYAN, ConsoleColors.RESET, statusPagamento
        ));

        sb.append(String.format("%sItens:%s%n", ConsoleColors.BRIGHT_WHITE, ConsoleColors.RESET));
        if (itens.isEmpty()) {
            sb.append(String.format("  %s(sem itens)%s%n", ConsoleColors.DIM, ConsoleColors.RESET));
        } else {
            for (ItemPedido item : itens) {
                sb.append("  - ").append(item.toString()).append("\n");
            }
        }

        sb.append(String.format("%sTotal:%s %sR$ %.2f%s%n",
                ConsoleColors.BRIGHT_WHITE, ConsoleColors.RESET,
                ConsoleColors.BRIGHT_GREEN, getTotal(), ConsoleColors.RESET));

        sb.append(String.format("%s=========================%s%n", ConsoleColors.BRIGHT_BLUE, ConsoleColors.RESET));
        return ConsoleColors.clean(sb.toString());
    }

    public void aplicarCupom(CupomDesconto cupom) {
        if (this.statusPedido != StatusPedido.ABERTO) {
            throw new IllegalStateException("Só é possível aplicar cupom em pedidos abertos");
        }
        if (!cupom.isValido()) {
            throw new IllegalStateException("Cupom inválido, expirado ou já utilizado");
        }
        this.cupomAplicado = cupom;
        recalcularValorTotal();
    }

    public void aplicarRegraDesconto(RegraDesconto regra) {
        if (this.statusPedido != StatusPedido.ABERTO) {
            throw new IllegalStateException("Só é possível aplicar regras em pedidos abertos");
        }
        this.regrasDesconto.add(regra);
        recalcularValorTotal();
    }

    private void recalcularValorTotal() {
        double valorBase = itens.stream()
                .mapToDouble(i -> i.getPrecoVenda() * i.getQuantidade())
                .sum();

        double descontoTotal = 0.0;

        // Aplicar cupom
        if (cupomAplicado != null && cupomAplicado.isValido()) {
            descontoTotal += cupomAplicado.calcularDesconto(valorBase);
        }

        // Aplicar regras de desconto
        for (RegraDesconto regra : regrasDesconto) {
            descontoTotal += regra.calcularDesconto(this);
        }

        this.valorDesconto = descontoTotal;
    }




}
