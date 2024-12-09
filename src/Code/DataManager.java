package Code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String BaseFolder = "Datos/";
    private static final String UsersFolder = BaseFolder + "Usuarios/";
    private static final String UserFile = BaseFolder + "usuarios.dat";
    private static final String GameFile = BaseFolder + "games.dat";
    private static final String MusicFile = BaseFolder + "music.dat";

    public DataManager() {
        File baseDir = new File(BaseFolder);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        File usersDir = new File(UsersFolder);
        if (!usersDir.exists()) {
            usersDir.mkdirs();
        }

        File userFile = new File(UserFile);
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                System.out.println("eeeee");
            }
        }

        File storeFile = new File(GameFile);
        if (!storeFile.exists()) {
            try {
                storeFile.createNewFile();
            } catch (IOException e) {
                System.out.println("eeeee");
            }
        }
    }

    public boolean register(String username, String password, boolean isAdmin) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(UserFile, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                String existingUser = raf.readUTF();
                raf.readUTF();
                raf.readBoolean();
                if (existingUser.equals(username)) {
                    return false;
                }
            }

            raf.seek(raf.length());
            raf.writeUTF(username);
            raf.writeUTF(password);
            raf.writeBoolean(isAdmin);

            File userDir = new File(UsersFolder + username);
            if (!userDir.exists()) {
                userDir.mkdirs();

                new File(userDir, "musica").mkdirs();
                new File(userDir, "juegos").mkdirs();
                new File(userDir, "chat").mkdirs();
            }

            return true;
        }
    }

    public boolean login(String username, String password) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(UserFile, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                String existingUser = raf.readUTF();
                String existingPassword = raf.readUTF();
                raf.readBoolean();
                if (existingUser.equals(username) && existingPassword.equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

   

    

    public void addGame(String username, Juego juego) throws IOException {
        File gamesFile = new File(UsersFolder + username + "/juegos.dat");
        if (!gamesFile.exists()) {
            gamesFile.createNewFile();
        }

        try (RandomAccessFile raf = new RandomAccessFile(gamesFile, "rw")) {
            raf.seek(raf.length());
            juego.savegame(raf);
        }
    }

    public List<Juego> listGames(String username) throws IOException {
        List<Juego> gameList = new ArrayList<>();
        File gamesFile = new File(UsersFolder + username + "/juegos.dat");
        if (!gamesFile.exists()) {
            return gameList;
        }

        try (RandomAccessFile raf = new RandomAccessFile(gamesFile, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                gameList.add(Juego.loadgame(raf));
            }
        }

        return gameList;
    }

    public boolean addGameToStore(String nombre, String genero, String desarrollador, String fechaLanzamiento, String rutaImagen) throws IOException {
        synchronized (this) {
            List<Juego> existingGames = listGamesFromStore();
            for (Juego existing : existingGames) {
                if (existing.getNombre().equalsIgnoreCase(nombre)) {
                    return false;
                }
            }

            try (RandomAccessFile raf = new RandomAccessFile(GameFile, "rw")) {
                raf.seek(raf.length());
                Juego newGame = new Juego(nombre, genero, desarrollador, fechaLanzamiento, rutaImagen);
                newGame.savegame(raf);
            }

            return true;
        }
    }

    public List<Juego> listGamesFromStore() throws IOException {
        List<Juego> gameList = new ArrayList<>();
        File gamesFile = new File(GameFile);
        if (!gamesFile.exists()) {
            return gameList;
        }

        try (RandomAccessFile raf = new RandomAccessFile(gamesFile, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                gameList.add(Juego.loadgame(raf));
            }
        }

        return gameList;
    }

    public boolean isAdmin(String username) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(UserFile, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                String existingUser = raf.readUTF();
                raf.readUTF();
                boolean isAdmin = raf.readBoolean();
                if (existingUser.equals(username)) {
                    return isAdmin;
                }
            }
        }
        return false;
    }

    public void clearUserGames(String username) throws IOException {
        File gamesFile = new File(UsersFolder + username + "/juegos.dat");
        if (gamesFile.exists()) {
            try (RandomAccessFile raf = new RandomAccessFile(gamesFile, "rw")) {
                raf.setLength(0);
            }
        }
    }

    
}
