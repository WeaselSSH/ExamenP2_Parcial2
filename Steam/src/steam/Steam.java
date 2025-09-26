package steam;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;

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
        rgames.writeInt(0);
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

    public boolean downloadGame(int gameCode, int clientCode, char sistemaOperativo) throws IOException {
        rgames.seek(0);
        boolean gCodeExists = false;
        char os = 'n';
        int edadMinima = 0;
        long nacimiento = 0;
        double precio = 0;
        String username = "";
        String title = "";
        String path = "";
        long posClient = 0;
        long posGame = 0;
        boolean activo = false;

        while (rgames.getFilePointer() < rgames.length()) {
            int tmpCode = rgames.readInt();
            long tmpPosGame = rgames.getFilePointer();
            String tmpTitle = rgames.readUTF();
            rgames.readUTF();
            char tmpos = rgames.readChar();
            int tmpedadMinima = rgames.readInt();
            double tmpPrecio = rgames.readDouble();
            rgames.readInt();
            String tmpPath = rgames.readUTF();

            if (tmpCode == gameCode) {
                gCodeExists = true;
                os = tmpos;
                edadMinima = tmpedadMinima;
                title = tmpTitle;
                path = tmpPath;
                precio = tmpPrecio;
                posGame = tmpPosGame;
            }
        }

        rplayer.seek(0);
        boolean cCodeExists = false;

        while (rplayer.getFilePointer() < rplayer.length()) {
            int tmpCode = rplayer.readInt();
            long tmpPosClient = rplayer.getFilePointer();
            String usernameTmp = rplayer.readUTF();
            rplayer.readUTF();
            rplayer.readUTF();
            long tmpNacimiento = rplayer.readLong();
            rplayer.readInt();
            rplayer.readUTF();
            rplayer.readUTF();
            boolean tmpActivo = rplayer.readBoolean();

            if (tmpCode == clientCode) {
                cCodeExists = true;
                nacimiento = tmpNacimiento;
                username = usernameTmp;
                activo = tmpActivo;
                posClient = tmpPosClient;
            }
        }

        Date edadJugador = new Date(nacimiento);

        Calendar cal = Calendar.getInstance();
        cal.setTime(edadJugador);
        Calendar hoy = Calendar.getInstance();

        int edad = hoy.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }

        /*
        Formato download_codigodownload.stm
        int downloadCode;
        int playerCode;
        String playerName;
        int gameCode;
        String gameName;
        Image gameImage;
        Double gamePrice;
        long fechaDownload;
         */
        if (activo == true && gCodeExists == true && cCodeExists == true && os == sistemaOperativo && edad >= edadMinima) {
            int downloadCode = getCode(3);
            RandomAccessFile rdownloads = new RandomAccessFile("steam/downloads/download_" + downloadCode + ".stm", "rw");

            rdownloads.writeInt(downloadCode);
            rdownloads.writeInt(clientCode);
            rdownloads.writeUTF(username);
            rdownloads.writeInt(gameCode);
            rdownloads.writeUTF(title);
            rdownloads.writeUTF(path);
            rdownloads.writeDouble(precio);
            rdownloads.writeLong(Calendar.getInstance().getTimeInMillis());

            rgames.seek(posGame);
            rgames.readUTF();
            rgames.readUTF();
            rgames.readChar();
            rgames.readInt();
            rgames.readDouble();
            long posDG = rgames.getFilePointer();
            int cdownloads = rgames.readInt();
            rgames.seek(posDG);
            rgames.writeInt(cdownloads + 1);

            rplayer.seek(posClient);
            rplayer.readUTF();
            rplayer.readUTF();
            rplayer.readUTF();
            rplayer.readLong();
            long posDP = rplayer.getFilePointer();
            int cdownloadsp = rplayer.readInt();
            rplayer.seek(posDP);
            rplayer.writeInt(cdownloadsp + 1);

            return true;
        } else {
            return false;
        }
    }

    public String login(String username, String password) throws IOException {
        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            int code = rplayer.readInt();
            String fileUsername = rplayer.readUTF();
            String filePassword = rplayer.readUTF();
            String nombre = rplayer.readUTF();
            long nacimiento = rplayer.readLong();
            int contDescargas = rplayer.readInt();
            String imgPath = rplayer.readUTF();
            String tipoUsuario = rplayer.readUTF();
            boolean estado = rplayer.readBoolean();

            if (fileUsername.equals(username) && filePassword.equals(password)) {
                return estado ? tipoUsuario : "INACTIVE";
            }
        }
        return null;
    }

    public boolean updatePriceFor(int codeGame, double newPrice) throws IOException {
        rgames.seek(0);
        while (rgames.getFilePointer() < rgames.length()) {
            int code = rgames.readInt();
            rgames.readUTF();
            rgames.readUTF();
            rgames.readChar();
            rgames.readInt();
            long pricePos = rgames.getFilePointer();
            rgames.readDouble();
            rgames.readInt();
            rgames.readUTF();

            if (code == codeGame) {
                rgames.seek(pricePos);
                rgames.writeDouble(newPrice);
                return true;
            }
        }
        return false;
    }

    public record Player(int code, String username, String password, String fullName, long birthDate, int downloads, String photoPath, String userType, boolean isActive) {

    }

    public Player getPlayerByUsername(String username) throws IOException {
        rplayer.seek(0);
        while (rplayer.getFilePointer() < rplayer.length()) {
            int code = rplayer.readInt();
            String fileUsername = rplayer.readUTF();
            String filePassword = rplayer.readUTF();
            String nombre = rplayer.readUTF();
            long nacimiento = rplayer.readLong();
            int contDescargas = rplayer.readInt();
            String imgPath = rplayer.readUTF();
            String tipoUsuario = rplayer.readUTF();
            boolean estado = rplayer.readBoolean();
            if (fileUsername.equals(username)) {
                return new Player(code, fileUsername, filePassword, nombre, nacimiento, contDescargas, imgPath, tipoUsuario, estado);
            }
        }
        return null;
    }

    public static Steam getINSTANCE() {
        return INSTANCE;
    }
}


//reporte se guarde en listas enlazadas
