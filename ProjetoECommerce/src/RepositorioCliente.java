import java.util.Optional;

public class RepositorioCliente extends RepositorioBase<Cliente>{

    private String maskDoc(String documento) {
        if (documento == null || documento.isBlank()) return "***";
        String digits = documento.replaceAll("\\D", "");
        if (digits.length() < 5) return "***";
        String inicio = digits.substring(0, 3);
        String fim = digits.substring(digits.length() - 2);
        String meio = "*".repeat(digits.length() - 5);
        return inicio + meio + fim;
    }

    @Override
    public void cadastrar(Cliente item) {
        if (item == null) throw new IllegalArgumentException("Cliente não pode ser nulo");
        boolean documentoJaExiste = lista.stream()
                .anyMatch(c -> c.getDocumento().equals(item.getDocumento()));
        if (documentoJaExiste) {
            throw new IllegalStateException("Já existe cliente com documento " + maskDoc(item.getDocumento()));
        }
        boolean emailJaExiste = lista.stream()
                .anyMatch(c -> c.getEmail() != null && c.getEmail().equalsIgnoreCase(item.getEmail()));
        if (emailJaExiste) {
            throw new IllegalStateException("Já existe cliente com e-mail " + item.getEmail());
        }
        lista.add(item);
    }

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
        if (idx.isEmpty()) throw new IllegalStateException("Cliente não encontrado para documento " + maskDoc(item.getDocumento()));

        boolean emailEmUsoPorOutro = lista.stream()
                .anyMatch(c -> !c.getDocumento().equals(item.getDocumento())
                        && c.getEmail() != null
                        && item.getEmail() != null
                        && c.getEmail().equalsIgnoreCase(item.getEmail()));
        if (emailEmUsoPorOutro) {
            throw new IllegalStateException("E-mail já utilizado por outro cliente: " + item.getEmail());
        }

        lista.set(idx.get(), item);
    }
}
