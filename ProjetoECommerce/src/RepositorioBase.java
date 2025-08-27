import java.util.List;
import java.util.ArrayList;

public abstract class RepositorioBase<T>  implements IRepositorio<T>{
    protected List<T> lista = new ArrayList<>();

    public void cadastrar(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        lista.add(item);
    }

    public List<T> listar(){
        return lista;
    }

    public abstract void atualizar(T item);

}
