import java.util.Optional;

public class RepositorioProduto extends RepositorioBase<Produto> {

    @Override
    public void cadastrar(Produto item) {
        if (item == null) throw new IllegalArgumentException("Produto não pode ser nulo");
        boolean idJaExiste = lista.stream().anyMatch(p -> p.getId() == item.getId());
        if (idJaExiste) {
            throw new IllegalStateException("Já existe produto com id " + item.getId());
        }
        lista.add(item);
    }

    @Override
    public void atualizar(Produto item) {
        if (item == null) throw new IllegalArgumentException("Produto não pode ser nulo");
        Optional<Integer> idx = Optional.empty();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == item.getId()) {
                idx = Optional.of(i);
                break;
            }
        }
        if (idx.isEmpty()) throw new IllegalStateException("Produto não encontrado para id " + item.getId());
        lista.set(idx.get(), item);
    }
}
