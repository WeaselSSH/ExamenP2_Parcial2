package steam;

public final class GameNode {

    public int code;
    public String titulo;
    public String genero;
    public char so;
    public int edadMin;
    public double precio;
    public int dls;
    public String path;

    public GameNode next;

    public GameNode(int code, String titulo, String genero, char so, int edadMin,
            double precio, int dls, String path) {
        this.code = code;
        this.titulo = titulo;
        this.genero = genero;
        this.so = so;
        this.edadMin = edadMin;
        this.precio = precio;
        this.dls = dls;
        this.path = path;
    }
}
