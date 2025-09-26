package steam;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Steam {

    private RandomAccessFile rcods, rgames, rplayer;

    private static final Steam INSTANCE = new Steam();

    private Steam() {
        try {
            File dir = new File("steam/downloads");
            if (!dir.mkdirs() && !dir.exists()) {
                throw new IOException("No se pudo crear steam/downloads");
            }

            rcods = new RandomAccessFile("steam/codes.stm", "rw");
            rgames = new RandomAccessFile("steam/games.stm", "rw");
            rplayer = new RandomAccessFile("steam/player.stm", "rw");

            initCodes();
        } catch (IOException e) {
            throw new RuntimeException("Fallo inicializando archivos: " + e.getMessage());
        }
    }

    /*
    Formato code: 
    int codeJuego
    int codeClientes
    int codeDownloads
     */
    private void initCodes() throws IOException {
        if (rcods.length() == 0) {
            rcods.writeInt(1);
            rcods.writeInt(1);
            rcods.writeInt(1);
        }
    }

    private int getCode(int opt) throws IOException {
        rcods.seek(0);
        int codeJuego = rcods.readInt();

        long posCliente = rcods.getFilePointer();
        int codeClientes = rcods.readInt();

        long posDownloads = rcods.getFilePointer();
        int codeDownloads = rcods.readInt();

        int code;

        switch (opt) {
            case 1:
                code = codeJuego;
                rcods.seek(0);
                rcods.writeInt(codeJuego + 1);
                return code;
            case 2:
                code = codeClientes;
                rcods.seek(posCliente);
                rcods.writeInt(codeClientes + 1);
                return code;
            case 3:
                code = codeDownloads;
                rcods.seek(posDownloads);
                rcods.writeInt(codeDownloads + 1);
                return code;
            default:
                return -1;
        }
    }

    /*
    Formato: games.stm
    int code;
    String titulo;
    String genero;
    char sistemaOperativo;
    int edadMinima;
    double precio;
    int cDownloads;
    String path;
     */
    public void addGame(String title, String genre, char os, int edadMinima, double precio,
            String path) throws IOException {

        rgames.seek(rgames.length());
        rgames.writeInt(getCode(1));
        rgames.writeUTF(title);
        rgames.writeUTF(genre);
        rgames.writeChar(os);
        rgames.writeInt(edadMinima);
        rgames.writeDouble(precio);
        rgames.writeUTF(path);
    }

    /*
    Formato: player.stm
    int code;
    String username;
    String password;
    String nombre;
    long nacimiento;
    int contadorDownloads;
    String path;
    String tipoUsuario;
    Boolean estado;
     */
    public void addPlayer(String u, String pass, String nombre, long nacimiento, String path,
            String tipoUsuario) throws IOException {

        rplayer.seek(rplayer.length());
        rplayer.writeInt(getCode(2));
        rplayer.writeUTF(u);
        rplayer.writeUTF(pass);
        rplayer.writeUTF(nombre);
        rplayer.writeLong(nacimiento);
        rplayer.writeInt(0);
        rplayer.writeUTF(path);
        rplayer.writeUTF(tipoUsuario);
        rplayer.writeBoolean(true);
    }

    public void downloadGame(int gameCode, int clientCode, char sistemaOperativo) {

    }

    public static Steam getINSTANCE() {
        return INSTANCE;
    }
}


//reporte se guarde en listas enlazadas
