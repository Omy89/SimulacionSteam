package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import Code.Music;
import Code.MusicManager;
import javazoom.jl.player.Player; // Asegúrate de tener JLayer en tu proyecto
import java.io.FileInputStream;

public class MusicLibrary extends JPanel implements ActionListener {

    private JList<String> libraryList;
    private DefaultListModel<String> libraryListModel;
    private JButton removeFromLibraryBtn, playBtn, pauseBtn, stopBtn;
    private MusicManager musicManager;
    private String currentUser;

    private Player player; // Reproductor JLayer
    private Thread playerThread; // Hilo para la reproducción
    private boolean isPaused = false;
    private String currentFilePath;

    public MusicLibrary(MusicManager musicManager, String username) {
        this.musicManager = musicManager;
        this.currentUser = username;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel de lista de canciones
        libraryListModel = new DefaultListModel<>();
        libraryList = new JList<>(libraryListModel);
        libraryList.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(libraryList);

        // Botones
        removeFromLibraryBtn = new JButton("Eliminar de Librería");
        playBtn = new JButton("Reproducir");
        pauseBtn = new JButton("Pausar");
        stopBtn = new JButton("Detener");

        removeFromLibraryBtn.addActionListener(this);
        playBtn.addActionListener(this);
        pauseBtn.addActionListener(this);
        stopBtn.addActionListener(this);

        // Panel inferior para los botones
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(playBtn);
        buttonPanel.add(pauseBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(removeFromLibraryBtn);

        // Agregar componentes al panel principal
        add(new JLabel("Mi Librería"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Cargar canciones de la librería
        loadLibrary();
    }

    public void loadLibrary() {
        try {
            List<Music> userLibrary = musicManager.getUserLibrary(currentUser);
            libraryListModel.clear();
            for (Music music : userLibrary) {
                libraryListModel.addElement(music.getTitle() + " - " + music.getArtist());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la librería.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == removeFromLibraryBtn) {
            removeFromLibrary();
        } else if (evt.getSource() == playBtn) {
            playMusic();
        } else if (evt.getSource() == pauseBtn) {
            pauseMusic();
        } else if (evt.getSource() == stopBtn) {
            stopMusic();
        }
    }

    private void removeFromLibrary() {
        int selectedIndex = libraryList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una canción para eliminar.");
            return;
        }

        try {
            String selectedSong = libraryListModel.getElementAt(selectedIndex);
            String[] songDetails = selectedSong.split(" - ");
            String title = songDetails[0];
            String artist = songDetails[1];

            // Eliminar canción de la librería
            List<Music> userLibrary = musicManager.getUserLibrary(currentUser);
            for (Music music : userLibrary) {
                if (music.getTitle().equals(title) && music.getArtist().equals(artist)) {
                    userLibrary.remove(music);
                    libraryListModel.remove(selectedIndex);
                    JOptionPane.showMessageDialog(this, "Canción eliminada de la librería.");
                    return;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la canción.");
        }
    }

    private void playMusic() {
        int selectedIndex = libraryList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una canción para reproducir.");
            return;
        }

        try {
            String selectedSong = libraryListModel.getElementAt(selectedIndex);
            String[] songDetails = selectedSong.split(" - ");
            String title = songDetails[0];
            String artist = songDetails[1];

            // Obtener la canción seleccionada
            List<Music> userLibrary = musicManager.getUserLibrary(currentUser);
            for (Music music : userLibrary) {
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
