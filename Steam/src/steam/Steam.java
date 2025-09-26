package steam;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
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

    public record Player(int code, String username, String password, String fullName, long birthDate, int downloads, String photoPath, String userType, boolean isActive) {}

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
        GameNode gameToDownload = findGameByCode(gameCode);
        Player downloadingPlayer = findPlayerByCode(clientCode);

        if (gameToDownload == null || downloadingPlayer == null || !downloadingPlayer.isActive()) return false;
        
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTimeInMillis(downloadingPlayer.birthDate());
        int age = Calendar.getInstance().get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
        if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        if (gameToDownload.so != sistemaOperativo || age < gameToDownload.edadMin) return false;

        int downloadCode = getCode(3);
        try (RandomAccessFile rdownloads = new RandomAccessFile("steam/downloads/download_" + downloadCode + ".stm", "rw")) {
            rdownloads.writeInt(downloadCode);
            rdownloads.writeInt(clientCode);
            rdownloads.writeUTF(downloadingPlayer.fullName());
            rdownloads.writeInt(gameCode);
            rdownloads.writeUTF(gameToDownload.titulo);
            rdownloads.writeUTF(gameToDownload.path);
            rdownloads.writeDouble(gameToDownload.precio);
            rdownloads.writeLong(System.currentTimeMillis());
        }

        updateDownloadCount(gameCode, clientCode);
        return true;
    }
    
    private void updateDownloadCount(int gameCode, int clientCode) throws IOException {
        long gamePos = findGamePosition(gameCode);
        if (gamePos != -1) {
            rgames.seek(gamePos);
            GameNode g = readGameNode();
            long currentPos = rgames.getFilePointer();
            rgames.seek(currentPos - g.path.length() - 2 - 4);
            rgames.writeInt(g.dls + 1);
        }

        long playerPos = findPlayerPosition(clientCode);
        if(playerPos != -1) {
            rplayer.seek(playerPos);
            Player p = readPlayer();
            long currentPos = rplayer.getFilePointer();
            rplayer.seek(currentPos - p.photoPath().length() - 2 - p.userType().length() - 2 - 1 - 4);
            rplayer.writeInt(p.downloads() + 1);
        }
    }

    public String login(String username, String password) throws IOException {
        Nodo<Player> players = printPlayers();
        Nodo<Player> current = players;
        while(current != null){
            if(current.data.username().equals(username) && current.data.password().equals(password)){
                return current.data.isActive() ? current.data.userType() : "INACTIVE";
            }
            current = current.siguiente;
        }
        return null;
    }

    public boolean updatePriceFor(int codeGame, double newPrice) throws IOException {
        long gamePos = findGamePosition(codeGame);
        if (gamePos != -1) {
            rgames.seek(gamePos);
            rgames.readInt(); 
            rgames.readUTF();
            rgames.readUTF();
            rgames.readChar();
            rgames.readInt();
            rgames.writeDouble(newPrice);
            return true;
        }
        return false;
    }
    
    public Player getPlayerByUsername(String username) throws IOException {
        Nodo<Player> players = printPlayers();
        Nodo<Player> current = players;
        while(current != null){
            if(current.data.username().equals(username)){
                return current.data;
            }
            current = current.siguiente;
        }
        return null;
    }
    
    public GameNode printGames() throws IOException {
        GameNode head = null, tail = null;
        rgames.seek(0);
        while(rgames.getFilePointer() < rgames.length()) {
            GameNode newNode = readGameNode();
            if(head == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
        }
        return head;
    }
    
    public Nodo<Player> printPlayers() throws IOException {
        Nodo<Player> head = null, tail = null;
        rplayer.seek(0);
        while(rplayer.getFilePointer() < rplayer.length()){
            Player p = readPlayer();
            Nodo<Player> newNode = new Nodo<>(p);
            if(head == null) {
                head = tail = newNode;
            } else {
                tail.siguiente = newNode;
                tail = newNode;
            }
        }
        return head;
    }

    public boolean updatePlayerStatus(int playerCode, boolean newStatus) throws IOException {
        long playerPos = findPlayerPosition(playerCode);
        if(playerPos != -1){
            rplayer.seek(playerPos);
            readPlayer();
            long finalPos = rplayer.getFilePointer();
            rplayer.seek(finalPos - 1); 
            rplayer.writeBoolean(newStatus);
            return true;
        }
        return false;
    }

    public boolean deleteGame(int gameCode) throws IOException {
        File originalFile = new File("steam/games.stm");
        File tempFile = new File("steam/games_temp.stm");
        boolean deleted = false;
        
        try (RandomAccessFile rafTemp = new RandomAccessFile(tempFile, "rw")) {
            rgames.seek(0);
            while (rgames.getFilePointer() < rgames.length()) {
                long startPos = rgames.getFilePointer();
                GameNode currentGame = readGameNode();
                
                if (currentGame.code != gameCode) {
                    rgames.seek(startPos);
                    byte[] recordData = new byte[getGameRecordSize(currentGame)];
                    rgames.read(recordData);
                    rafTemp.write(recordData);
                } else {
                    deleted = true;
                }
            }
        }
        
        rgames.close();

        if(originalFile.delete()){
            tempFile.renameTo(originalFile);
        } else {
             rgames = new RandomAccessFile(originalFile, "rw");
             throw new IOException("No se pudo reemplazar el archivo de juegos.");
        }
        
        rgames = new RandomAccessFile(originalFile, "rw");
        return deleted;
    }
    
    public boolean reportForClient(int codeClient, String txtFile) throws IOException {
        Player client = findPlayerByCode(codeClient);
        if (client == null) {
            return false;
        }

        DownloadNode downloadHistory = null;
        File downloadsFolder = new File("steam/downloads");
        File[] downloadFiles = downloadsFolder.listFiles();

        if (downloadFiles != null) {
            for (File file : downloadFiles) {
                if (file.isFile() && file.getName().startsWith("download_")) {
                    try (RandomAccessFile rafDownload = new RandomAccessFile(file, "r")) {
                        int downloadId = rafDownload.readInt();
                        int playerCodeInFile = rafDownload.readInt();
                        
                        if (playerCodeInFile == codeClient) {
                            rafDownload.readUTF();
                            int gameCode = rafDownload.readInt();
                            String gameName = rafDownload.readUTF();
                            rafDownload.readUTF();
                            double price = rafDownload.readDouble();
                            long date = rafDownload.readLong();
                            String genre = findGameByCode(gameCode).genero;
                            
                            DownloadNode newNode = new DownloadNode(downloadId, gameCode, gameName, genre, price, date);
                            newNode.next = downloadHistory;
                            downloadHistory = newNode;
                        }
                    }
                }
            }
        }

        try (PrintWriter writer = new PrintWriter(new File("steam/" + txtFile))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTimeInMillis(client.birthDate());
            int age = Calendar.getInstance().get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
            if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) age--;
            
            writer.println("REPORTE CLIENTE: " + client.fullName() + " (username: " + client.username() + ")");
            writer.println("Codigo cliente: " + client.code());
            writer.println("Fecha de nacimiento: " + dateFormat.format(new Date(client.birthDate())) + " (" + age + " anos)");
            writer.println("Estado: " + (client.isActive() ? "ACTIVO" : "DESACTIVO"));
            writer.println("Total downloads: " + client.downloads());
            writer.println();
            writer.println("HISTORIAL DE DESCARGAS:");
            writer.println("FECHA(YYYY-MM-DD) | DOWNLOAD ID | GAME CODE | GAME NAME | PRICE | GENRE");
            
            DownloadNode current = downloadHistory;
            while(current != null) {
                writer.printf("%s | %-11d | %-9d | %-25s | %-5.2f | %s%n",
                        dateFormat.format(new Date(current.fechaMillis)),
                        current.downloadId,
                        current.gameCode,
                        current.gameName,
                        current.price,
                        current.genre);
                current = current.next;
            }
        }
        
        System.out.println("REPORTE CREADO");
        return true;
    }
    
    private Player findPlayerByCode(int code) throws IOException {
        Nodo<Player> players = printPlayers();
        Nodo<Player> current = players;
        while(current != null) {
            if(current.data.code() == code) return current.data;
            current = current.siguiente;
        }
        return null;
    }
    
    private GameNode findGameByCode(int code) throws IOException {
        GameNode games = printGames();
        GameNode current = games;
        while(current != null) {
            if(current.code == code) return current;
            current = current.next;
        }
        return null;
    }
    
    private long findGamePosition(int code) throws IOException {
        rgames.seek(0);
        while(rgames.getFilePointer() < rgames.length()){
            long pos = rgames.getFilePointer();
            if(rgames.readInt() == code) return pos;
            rgames.seek(pos);
            readGameNode();
        }
        return -1;
    }
    
    private long findPlayerPosition(int code) throws IOException {
        rplayer.seek(0);
        while(rplayer.getFilePointer() < rplayer.length()){
            long pos = rplayer.getFilePointer();
            if(rplayer.readInt() == code) return pos;
            rplayer.seek(pos);
            readPlayer();
        }
        return -1;
    }
    
    private GameNode readGameNode() throws IOException {
        return new GameNode(rgames.readInt(), rgames.readUTF(), rgames.readUTF(), rgames.readChar(), rgames.readInt(), rgames.readDouble(), rgames.readInt(), rgames.readUTF());
    }

    private Player readPlayer() throws IOException {
        return new Player(rplayer.readInt(), rplayer.readUTF(), rplayer.readUTF(), rplayer.readUTF(), rplayer.readLong(), rplayer.readInt(), rplayer.readUTF(), rplayer.readUTF(), rplayer.readBoolean());
    }

    private int getGameRecordSize(GameNode node) {
        return 4 + (2 + node.titulo.getBytes().length) + (2 + node.genero.getBytes().length) + 2 + 4 + 8 + 4 + (2 + node.path.getBytes().length);
    }
    
    public static Steam getINSTANCE() {
        return INSTANCE;
    }
}