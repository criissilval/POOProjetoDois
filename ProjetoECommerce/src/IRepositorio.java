import java.util.List;

public interface IRepositorio<T> {
    void cadastrar(T item);
    List<T> listar();
    void atualizar(T item);

}
