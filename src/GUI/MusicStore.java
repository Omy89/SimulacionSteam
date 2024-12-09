package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import Code.Music;
import Code.MusicManager;
import javazoom.jl.player.Player; // Asegúrate de tener JLayer en tu proyecto
import java.io.FileInputStream;

public class MusicStore extends JPanel implements ActionListener {

    private JList<String> storeList;
    private DefaultListModel<String> storeListModel;
    private JButton addToLibraryBtn, playBtn, pauseBtn, stopBtn;
    private MusicManager musicManager;
    private String currentUser;

    private Player player; // Reproductor JLayer
    private Thread playerThread; // Hilo para la reproducción
    private boolean isPaused = false;
    private String currentFilePath;

    public MusicStore(MusicManager musicManager, String username) {
        this.musicManager = musicManager;
        this.currentUser = username;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel de lista de canciones
        storeListModel = new DefaultListModel<>();
        storeList = new JList<>(storeListModel);
        storeList.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(storeList);

        // Botones
        addToLibraryBtn = new JButton("Añadir a Librería");
        playBtn = new JButton("Reproducir");
        pauseBtn = new JButton("Pausar");
        stopBtn = new JButton("Detener");

        addToLibraryBtn.addActionListener(this);
        playBtn.addActionListener(this);
        pauseBtn.addActionListener(this);
        stopBtn.addActionListener(this);

        // Panel inferior para los botones
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(playBtn);
        buttonPanel.add(pauseBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(addToLibraryBtn);

        // Agregar componentes al panel principal
        add(new JLabel("Almacén de Música"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Cargar canciones del almacén
        loadMusicStore();
    }

    public void loadMusicStore() {
        try {
            List<Music> allMusic = musicManager.getAllMusic();
            storeListModel.clear();
            for (Music music : allMusic) {
                storeListModel.addElement(music.getTitle() + " - " + music.getArtist());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las canciones.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == addToLibraryBtn) {
            addToLibrary();
        } else if (evt.getSource() == playBtn) {
            playMusic();
        } else if (evt.getSource() == pauseBtn) {
            pauseMusic();
        } else if (evt.getSource() == stopBtn) {
            stopMusic();
        }
    }

    private void addToLibrary() {
        int selectedIndex = storeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una canción para añadir a la librería.");
            return;
        }

        try {
            String selectedSong = storeListModel.getElementAt(selectedIndex);
            String[] songDetails = selectedSong.split(" - ");
            String title = songDetails[0];
            String artist = songDetails[1];

            List<Music> allMusic = musicManager.getAllMusic();
            for (Music music : allMusic) {
                if (music.getTitle().equals(title) && music.getArtist().equals(artist)) {
                    if (musicManager.downloadMusic(currentUser, music)) {
                        JOptionPane.showMessageDialog(this, "Canción añadida a la librería.");
                    } else {
                        JOptionPane.showMessageDialog(this, "La canción ya está en tu librería.");
                    }
                    return;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al añadir la canción a la librería.");
        }
    }

    private void playMusic() {
        int selectedIndex = storeList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una canción para reproducir.");
            return;
        }

        try {
            String selectedSong = storeListModel.getElementAt(selectedIndex);
            String[] songDetails = selectedSong.split(" - ");
            String title = songDetails[0];
            String artist = songDetails[1];

            // Obtener la canción seleccionada
            List<Music> allMusic = musicManager.getAllMusic();
            for (Music music : allMusic) {
                if (music.getTitle().equals(title) && music.getArtist().equals(artist)) {
                    stopMusic(); // Detener cualquier reproducción actual
                    currentFilePath = music.getFilePath();
                    FileInputStream fis = new FileInputStream(currentFilePath);
                    player = new Player(fis);

                    // Crear un hilo para la reproducción
                    playerThread = new Thread(() -> {
                        try {
                            player.play();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Error al reproducir la canción.");
                        }
                    });
                    playerThread.start();
                    JOptionPane.showMessageDialog(this, "Reproduciendo: " + title);
                    return;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al reproducir la canción.");
        }
    }

    private void pauseMusic() {
        if (player != null && !isPaused) {
            stopMusic();
            isPaused = true;
            JOptionPane.showMessageDialog(this, "Música pausada.");
        }
    }

    private void stopMusic() {
        if (player != null) {
            player.close();
            player = null;
            currentFilePath = null;
            isPaused = false;
            if (playerThread != null) {
                playerThread.interrupt();
            }
            JOptionPane.showMessageDialog(this, "Música detenida.");
        }
    }
}
