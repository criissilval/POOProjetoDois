public class RegraDescontoSimples implements RegraDesconto{
    private double valorDesconto;
    private TipoDesconto tipo;
    private String descricao;

    public RegraDescontoSimples(double valorDesconto, TipoDesconto tipo, String descricao){
        this.valorDesconto = valorDesconto;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    @Override
    public double calcularDesconto(Pedido pedido) {
        double valorTotal = pedido.getTotal();
        if(tipo == TipoDesconto.FIXO){
            return Math.min(valorDesconto, valorTotal);
        }else {
            return valorTotal * (valorDesconto / 100.0);
        }
    }
    @Override
    public String getDescricao() {return descricao;}
}
