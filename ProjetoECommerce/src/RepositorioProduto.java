public class RepositorioProduto extends RepositorioBase<Produtos> {

    @Override
    public void atualizar(Produtos produtos) {
        for (Produtos p : lista) {
            if (p.getId() == produtos.getId()) {
                p.setNome(produtos.getNome());
                p.setPreco(produtos.getPreco());
                break;
            }
        }

    }
}
