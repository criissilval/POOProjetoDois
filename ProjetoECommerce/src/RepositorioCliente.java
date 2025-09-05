import java.util.Optional;

public class RepositorioCliente extends RepositorioBase<Cliente>{

    @Override
    public void atualizar(Cliente item) {
        if (item == null) throw new IllegalStateException("Cliente não pode ser nulo");
        Optional<Integer> idx = Optional.empty();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getDocumento().equals(item.getDocumento())) {
                idx = Optional.of(i);
                break;
            }
        }
        if (idx.isEmpty()) throw new IllegalStateException("Cliente não encontrado para documento " + item.getDocumento());
        lista.set(idx.get(), item);
    }
}
