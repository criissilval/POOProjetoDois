import java.time.LocalDate;

public class CupomDesconto {
    private String codigo;
    private double valorDesconto;
    private TipoDesconto tipo;
    private LocalDate dataExpiracao;
    private boolean utilizado;
    private boolean ativo;

    public CupomDesconto(String codigo, double valorDesconto, TipoDesconto tipo, LocalDate dataExpiracao) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código do cupom não pode ser vazio");
        }
        if (valorDesconto <= 0) {
            throw new IllegalArgumentException("Valor de desconto deve ser maior que zero");
        }
        if (tipo == TipoDesconto.PERCENTUAL && valorDesconto > 100) {
            throw new IllegalArgumentException("Desconto percentual não pode ser maior que 100%");
        }
        if (dataExpiracao == null) {
            throw new IllegalArgumentException("Data de expiração é obrigatória");
        }

        this.codigo = codigo;
        this.valorDesconto = valorDesconto;
        this.tipo = tipo;
        this.dataExpiracao = dataExpiracao;
        this.utilizado = false;
        this.ativo = true;
    }

    // Getters e Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public double getValorDesconto() { return valorDesconto; }
    public void setValorDesconto(double valorDesconto) { this.valorDesconto = valorDesconto; }

    public TipoDesconto getTipo() { return tipo; }
    public void setTipo(TipoDesconto tipo) { this.tipo = tipo; }

    public LocalDate getDataExpiracao() { return dataExpiracao; }
    public void setDataExpiracao(LocalDate dataExpiracao) { this.dataExpiracao = dataExpiracao; }

    public boolean isUtilizado() { return utilizado; }
    public void setUtilizado(boolean utilizado) { this.utilizado = utilizado; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public boolean isValido() {
        return ativo && !utilizado && !LocalDate.now().isAfter(dataExpiracao);
    }

    public double calcularDesconto(double valorTotal) {
        if (!isValido()) {
            throw new IllegalStateException("Cupom inválido, expirado ou já utilizado");
        }

        if (tipo == TipoDesconto.FIXO) {
            return Math.min(valorDesconto, valorTotal);
        } else { // PERCENTUAL
            return valorTotal * (valorDesconto / 100.0);
        }
    }

    @Override
    public String toString() {
        String status = isValido() ? "✓ VÁLIDO" : "✗ INVÁLIDO";
        String tipoDesc = tipo == TipoDesconto.FIXO ? "R$ " + valorDesconto : valorDesconto + "%";
        return String.format("Cupom: %s | Desconto: %s | Expira: %s | Status: %s",
                codigo, tipoDesc, dataExpiracao, status);
    }
}
