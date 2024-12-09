package GUI;

import Code.DataManager;
import Code.GlobalChatManager;
import Code.Music;
import Code.MusicManager;
import Code.PrivateChatManager;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Steam extends JFrame {

    private CardLayout cardlayout;
    private JPanel cardpanel;
    private DataManager usermanager;
    private MusicManager MusicManager;

    // Referencias a las tiendas
    private GameStore gamestore;
    private MusicStore musicStore;

    public Steam(String loggedUser, boolean isAdmin) throws IOException {

        this.usermanager = new DataManager();
        this.MusicManager = new MusicManager();
        setTitle("Steam");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardlayout = new CardLayout();
        cardpanel = new JPanel(cardlayout);
        add(cardpanel);

        JMenuBar menuBar = new JMenuBar();

        JMenu tiendaMenu = new JMenu("Tienda");
        JMenuItem juegosItem = new JMenuItem("Juegos");
        JMenuItem musicaItem = new JMenuItem("Música");
        tiendaMenu.add(juegosItem);
        tiendaMenu.add(musicaItem);
        menuBar.add(tiendaMenu);

        JMenu libreriaMenu = new JMenu("Librería");
        JMenuItem libreriaJuegosItem = new JMenuItem("Juegos");
        JMenuItem libreriaMusicaItem = new JMenuItem("Música");
        libreriaMenu.add(libreriaJuegosItem);
        libreriaMenu.add(libreriaMusicaItem);
        menuBar.add(libreriaMenu);

        JMenu comunidadMenu = new JMenu("Comunidad");
        JMenuItem chatGlobalItem = new JMenuItem("Chat Global");
        JMenuItem chatsPrivadosItem = new JMenuItem("Chats Privados");
        comunidadMenu.add(chatGlobalItem);
        comunidadMenu.add(chatsPrivadosItem);
        menuBar.add(comunidadMenu);

        if (isAdmin) {
            JMenu adminMenu = new JMenu("Admin");
            JMenuItem agregarJuegoItem = new JMenuItem("Agregar Juego");
            JMenuItem agregarCancionItem = new JMenuItem("Agregar Canción");
            adminMenu.add(agregarJuegoItem);
            adminMenu.add(agregarCancionItem);
            menuBar.add(adminMenu);

            agregarJuegoItem.addActionListener(e -> agregarJuego());
            agregarCancionItem.addActionListener(e -> agregarMusica());
        }

        // Menú Usuario
        JMenu userMenu = new JMenu(loggedUser);
        JMenuItem cerrarSesionItem = new JMenuItem("Cerrar Sesión");
        JMenuItem ajustesItem = new JMenuItem("Ajustes");
        userMenu.add(cerrarSesionItem);
        userMenu.add(ajustesItem);
        menuBar.add(userMenu);

        setJMenuBar(menuBar);

        // Creación de los paneles principales
        gamestore = new GameStore(usermanager, loggedUser);
        GameLibrary gamelibrary = new GameLibrary(usermanager, loggedUser);

        // Agregar paneles al CardLayout
        cardpanel.add(gamestore, "gamestore");
        cardpanel.add(gamelibrary, "gamelibrary");

        musicStore = new MusicStore(MusicManager, loggedUser);
        MusicLibrary musicLibrary = new MusicLibrary(MusicManager, loggedUser);

        cardpanel.add(musicStore, "MusicStore");
        cardpanel.add(musicLibrary, "MusicLibrary");

        // Configuración de ActionListeners
        juegosItem.addActionListener(e -> cardlayout.show(cardpanel, "gamestore"));
        musicaItem.addActionListener(e -> cardlayout.show(cardpanel, "MusicStore"));
        libreriaJuegosItem.addActionListener(e -> {
            gamelibrary.cargarBiblioteca();
            cardlayout.show(cardpanel, "gamelibrary");
        });
        libreriaMusicaItem.addActionListener(e -> {
            musicLibrary.loadLibrary();
            cardlayout.show(cardpanel, "MusicLibrary");
        });

        chatGlobalItem.addActionListener(e -> {
            JFrame chatFrame = new JFrame("Chat Global");
            chatFrame.setSize(600, 400);
            chatFrame.setLocationRelativeTo(this);
            GlobalChatManager chatManager = new GlobalChatManager();
            GlobalChat chatPanel = new GlobalChat(chatManager, loggedUser);
            chatFrame.add(chatPanel);
            chatFrame.setVisible(true);
        });

        chatGlobalItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Chat Global (No implementado)"));
        chatsPrivadosItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Chats Privados (No implementado)"));
        cerrarSesionItem.addActionListener(e -> cerrarSesion());
        ajustesItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir Ajustes (No implementado)"));

        // Relación entre GameStore y GameLibrary
        gamestore.setLibraryReference(gamelibrary);
        
        chatsPrivadosItem.addActionListener(e -> {
    String recipient = JOptionPane.showInputDialog(this, "Ingresa el nombre del usuario:");
    if (recipient != null && !recipient.trim().isEmpty()) {
        if (!recipient.equals(loggedUser)) {
            JFrame chatFrame = new JFrame("Chat Privado con " + recipient);
            chatFrame.setSize(600, 400);
            chatFrame.setLocationRelativeTo(this);
            PrivateChatManager chatManager = new PrivateChatManager();
            PrivateChat chatPanel = new PrivateChat(chatManager, loggedUser, recipient);
            chatFrame.add(chatPanel);
            chatFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No puedes enviarte mensajes a ti mismo.");
        }
    }
});
    }

    private void cerrarSesion() {
        dispose();
        new StartMenu();
    }

    private void agregarJuego() {
        String name = JOptionPane.showInputDialog(this, "Ingrese el nombre del juego:");
        String genre = JOptionPane.showInputDialog(this, "Ingrese el género del juego:");
        String developer = JOptionPane.showInputDialog(this, "Ingrese el desarrollador del juego:");
        String releaseDate = JOptionPane.showInputDialog(this, "Ingrese la fecha de lanzamiento del juego:");

        if (name != null && genre != null && developer != null && releaseDate != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione una imagen para el juego");
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                File resourcesDir = new File("Resources");

                if (!resourcesDir.exists()) {
                    resourcesDir.mkdirs();
                }

                File destinationFile = new File(resourcesDir, name + "_" + selectedFile.getName());
                try {
                    Files.copy(selectedFile.toPath(), destinationFile.toPath());
                    String imagePath = destinationFile.getPath();
                    usermanager.addGameToStore(name, genre, developer, releaseDate, imagePath);

                    JOptionPane.showMessageDialog(this, "¡Juego agregado exitosamente!");

                    // Actualizar la vista de la tienda de juegos
                    gamestore.cargarJuegos();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al copiar la imagen o guardar el juego.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Operación cancelada. No se seleccionó ninguna imagen.");
            }
        }
    }

    private void agregarMusica() {
        String title = JOptionPane.showInputDialog(this, "Ingrese el título de la canción:");
        String artist = JOptionPane.showInputDialog(this, "Ingrese el nombre del artista:");
        String album = JOptionPane.showInputDialog(this, "Ingrese el álbum de la canción:");
        String durationStr = JOptionPane.showInputDialog(this, "Ingrese la duración de la canción (en segundos):");

        if (title != null && artist != null && album != null && durationStr != null) {
            try {
                int duration = Integer.parseInt(durationStr);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos MP3", "mp3"));
                fileChooser.setDialogTitle("Seleccione el archivo de música (MP3)");
                int result = fileChooser.showOpenDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    File musicDir = new File("MusicFiles");

                    if (!musicDir.exists()) {
                        musicDir.mkdirs();
                    }

                    File destinationFile = new File(musicDir, title + "_" + selectedFile.getName());
                    Files.copy(selectedFile.toPath(), destinationFile.toPath());

                    String filePath = destinationFile.getPath();

                    Music newMusic = new Music(title, artist, album, duration, filePath);
                    MusicManager.addMusic(newMusic);

                    JOptionPane.showMessageDialog(this, "¡Canción agregada exitosamente!");

                    // Actualizar la vista de la tienda de música
                    musicStore.loadMusicStore();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La duración debe ser un número entero válido.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al copiar el archivo de música o guardar los datos.");
            }
        }
    }
}
