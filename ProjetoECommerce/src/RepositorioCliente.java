public class RepositorioCliente extends RepositorioBase<Cliente>{

    @Override
    public void atualizar(Cliente cliente) {
        for (Cliente c : lista) {
            if (c.getDocumento().equals(cliente.getDocumento())) {
                c.setNome(cliente.getNome());
                c.setEmail(cliente.getEmail());
                break;
            }
        }
    }
}
