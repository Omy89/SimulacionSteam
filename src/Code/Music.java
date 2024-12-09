package Code;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Music {
    private String title;
    private String artist;
    private String album;
    private int duration; 
    private String filePath;

    public Music(String title, String artist, String album, int duration, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getDuration() {
        return duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void savemusic(RandomAccessFile raf) throws IOException {
        raf.writeUTF(title);
        raf.writeUTF(artist);
        raf.writeUTF(album);
        raf.writeInt(duration);
        raf.writeUTF(filePath);
    }

    public static Music loadmusic(RandomAccessFile raf) throws IOException {
        String title = raf.readUTF();
        String artist = raf.readUTF();
        String album = raf.readUTF();
        int duration = raf.readInt();
        String filePath = raf.readUTF();
        return new Music(title, artist, album, duration, filePath);
    }
}