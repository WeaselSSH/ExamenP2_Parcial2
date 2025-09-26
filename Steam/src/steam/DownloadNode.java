package steam;

public final class DownloadNode {

    public int downloadId;
    public int gameCode;
    public String gameName;
    public String genre;
    public double price;
    public long fechaMillis;

    public DownloadNode next;

    public DownloadNode(int downloadId, int gameCode, String gameName,
            String genre, double price, long fechaMillis) {
        this.downloadId = downloadId;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.genre = genre;
        this.price = price;
        this.fechaMillis = fechaMillis;
    }
}
