import java.util.ArrayList;
import java.util.List;

public class RegraDescontoComposta implements RegraDesconto {
    private List<RegraDesconto> regras;
    private String descricao;
    private TipoComposicao tipoComposicao; // SOMA ou MAIOR

    public RegraDescontoComposta(String descricao, TipoComposicao tipoComposicao) {
        this.regras = new ArrayList<>();
        this.descricao = descricao;
        this.tipoComposicao = tipoComposicao;
    }

    public void adicionarRegra(RegraDesconto regra) {
        regras.add(regra);
    }

    @Override
    public double calcularDesconto(Pedido pedido) {
        if (tipoComposicao == TipoComposicao.SOMA) {
            return regras.stream()
                    .mapToDouble(r -> r.calcularDesconto(pedido))
                    .sum();
        } else { // MAIOR
            return regras.stream()
                    .mapToDouble(r -> r.calcularDesconto(pedido))
                    .max()
                    .orElse(0.0);
        }
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
