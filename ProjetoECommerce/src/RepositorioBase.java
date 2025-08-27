import java.util.List;
import java.util.ArrayList;

public abstract class RepositorioBase<T> {
    protected List<T> lista = new ArrayList<>();

    public void cadastrar(T item){
        lista.add(item);
    }

    public List<T> listar(){
        return lista;
    }

    public abstract void atualizar(T item);

}
