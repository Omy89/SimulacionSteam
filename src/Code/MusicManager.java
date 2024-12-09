package Code;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class MusicManager {
    private static final String MusicFile = "Datos/music.dat";

    public MusicManager() {
        File musicFile = new File(MusicFile);
        if (!musicFile.exists()) {
            try {
                musicFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de música.");
            }
        }
    }

    public boolean addMusic(Music music) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(MusicFile, "rw")) {
           
            while (raf.getFilePointer() < raf.length()) {
                Music existingMusic = Music.loadmusic(raf);
                if (existingMusic.getTitle().equals(music.getTitle()) &&
                    existingMusic.getArtist().equals(music.getArtist())) {
                    return false; 
                }
            }

            raf.seek(raf.length());
            music.savemusic(raf);
            return true;
        }
    }

    public List<Music> getAllMusic() throws IOException {
        List<Music> musicList = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(MusicFile, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                musicList.add(Music.loadmusic(raf));
            }
        }
        return musicList;
    }

    public boolean downloadMusic(String username, Music music) throws IOException {
        File userMusicFile = new File("Datos/Usuarios/" + username + "/musica/library.dat");

        if (!userMusicFile.exists()) {
            userMusicFile.createNewFile();
        }

        try (RandomAccessFile raf = new RandomAccessFile(userMusicFile, "rw")) {
            
            while (raf.getFilePointer() < raf.length()) {
                Music existingMusic = Music.loadmusic(raf);
                if (existingMusic.getTitle().equals(music.getTitle()) &&
                    existingMusic.getArtist().equals(music.getArtist())) {
                    return false; 
                }
            }

       
            raf.seek(raf.length());
            music.savemusic(raf);
            return true;
        }
    }


    public List<Music> getUserLibrary(String username) throws IOException {
        List<Music> musicLibrary = new ArrayList<>();
        File userMusicFile = new File("Datos/Usuarios/" + username + "/musica/library.dat");

        if (!userMusicFile.exists()) {
            return musicLibrary; 
        }

        try (RandomAccessFile raf = new RandomAccessFile(userMusicFile, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                musicLibrary.add(Music.loadmusic(raf));
            }
        }
        return musicLibrary;
    }
}