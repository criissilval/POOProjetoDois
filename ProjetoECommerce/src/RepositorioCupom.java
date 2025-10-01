import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioCupom extends RepositorioBase<CupomDesconto> {

    @Override
    public void cadastrar(CupomDesconto item) {
        if (item == null) throw new IllegalArgumentException("Cupom não pode ser nulo");
        boolean codigoJaExiste = lista.stream()
                .anyMatch(c -> c.getCodigo().equalsIgnoreCase(item.getCodigo()));
        if (codigoJaExiste) {
            throw new IllegalStateException("Já existe cupom com código " + item.getCodigo());
        }
        lista.add(item);
    }

    @Override
    public void atualizar(CupomDesconto item) {
        if (item == null) throw new IllegalArgumentException("Cupom não pode ser nulo");
        Optional<Integer> idx = Optional.empty();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equalsIgnoreCase(item.getCodigo())) {
                idx = Optional.of(i);
                break;
            }
        }
        if (idx.isEmpty()) {
            throw new IllegalStateException("Cupom não encontrado para código " + item.getCodigo());
        }
        lista.set(idx.get(), item);
    }

    public Optional<CupomDesconto> buscarPorCodigo(String codigo) {
        return lista.stream()
                .filter(c -> c.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    public List<CupomDesconto> listarValidos() {
        return lista.stream()
                .filter(CupomDesconto::isValido)
                .collect(Collectors.toList());
    }
}