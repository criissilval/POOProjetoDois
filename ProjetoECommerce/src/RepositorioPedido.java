import java.util.Optional;

public class RepositorioPedido extends RepositorioBase<Pedido> {
    @Override
    public void atualizar(Pedido pedido) {
        if (pedido == null) throw new IllegalArgumentException("Pedido não pode ser nulo");
        Optional<Integer> idx = Optional.empty();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == pedido.getId()) {
                idx = Optional.of(i);
                break;
            }
        }
        if (idx.isEmpty()) throw new IllegalArgumentException("Pedido não encontrado para id " + pedido.getId());
        lista.set(idx.get(), pedido);
    }

    public Optional<Pedido> buscarPorId(int id) {
        return lista.stream().filter(p -> p.getId() == id).findFirst();
    }
}
