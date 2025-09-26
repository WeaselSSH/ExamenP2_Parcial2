package steam;

/**
 * Representa un nodo individual en una lista enlazada.
 * Es una clase gen�rica para poder almacenar cualquier tipo de dato.
 * @param <T> El tipo de dato que el nodo almacenar�.
 */
public class Nodo<T> {
    
    // El dato almacenado en el nodo (ej. un DownloadRecord)
    public T data;
    
    // La referencia o "flecha" que apunta al siguiente nodo en la lista
    public Nodo<T> siguiente;

    /**
     * Constructor para un nuevo nodo.
     * @param data El dato que se guardar� en este nodo.
     */
    public Nodo(T data) {
        this.data = data;
        this.siguiente = null; // Al crearse, un nodo no apunta a nada todav�a.
    }
}