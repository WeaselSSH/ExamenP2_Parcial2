package steam;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Steam {

    private RandomAccessFile rcods, rgames, rplayer;

    private static final Steam INSTANCE = new Steam();

    private Steam() {
        try {
            File mf = new File("steam");
            mf.mkdir();

            rcods = new RandomAccessFile("steam/codes.stm", "rw");
            rgames = new RandomAccessFile("steam/games.stm", "rw");
            rplayer = new RandomAccessFile("steam/player.stm", "rw");

            initCodes();
        } catch (IOException e) {
            System.out.println("Error.");
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

    private int getCodeJuego() throws IOException {
        rcods.seek(0);
        int codeJuego = rcods.readInt();
        int codeClientes = rcods.readInt();
        int codeDownloads = rcods.readInt();

        rcods.seek(0);
        rcods.writeInt(codeJuego + 1);
        rcods.writeInt(codeClientes + 1);
        rcods.writeInt(codeDownloads + 1);
        return codeJuego;
    }

    public static Steam getINSTANCE() {
        return INSTANCE;
    }
}


//reporte se guarde en listas enlazadas
