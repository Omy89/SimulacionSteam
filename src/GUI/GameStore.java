package GUI;

import Code.Juego;
import Code.DataManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GameStore extends JPanel {

    private DataManager userManager;
    private String loggedUser;

    public GameStore(DataManager userManager, String loggedUser) {
        this.userManager = userManager;
        this.loggedUser = loggedUser;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        cargarJuegos();
    }

    public void cargarJuegos() {
        removeAll();
        try {
            List<Juego> juegos = userManager.listGamesFromStore();

            for (Juego juego : juegos) {
                JPanel juegoPanel = crearPanelJuego(juego);
                add(juegoPanel);
                add(Box.createVerticalStrut(10));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos: " + e.getMessage());
        }
        revalidate();
        repaint();
    }

    private JPanel crearPanelJuego(Juego juego) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setMaximumSize(new Dimension(600, 200));

        ImageIcon icon = new ImageIcon(juego.getGamepath());
        JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        panel.add(imageLabel, BorderLayout.WEST);

        // Información del juego
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel("Nombre: " + juego.getNombre()));
        infoPanel.add(new JLabel("Género: " + juego.getGenero()));
        infoPanel.add(new JLabel("Desarrollador: " + juego.getDesarrollador()));
        infoPanel.add(new JLabel("Fecha de lanzamiento: " + juego.getFechaLanzamiento()));
        panel.add(infoPanel, BorderLayout.CENTER);

        JButton downloadButton = new JButton("Descargar");
        downloadButton.addActionListener(e -> descargarJuego(juego));
        panel.add(downloadButton, BorderLayout.EAST);

        return panel;
    }

    private void actualizarBiblioteca() {
        Container parent = this.getParent();
        if (parent instanceof JFrame) {
            JFrame frame = (JFrame) parent;
            for (Component comp : frame.getContentPane().getComponents()) {
                if (comp instanceof GameLibrary) {
                    ((GameLibrary) comp).cargarBiblioteca();
                    break;
                }
            }
        }
    }

    public void setLibraryReference(GameLibrary gamelibrary) {
        this.gamelibrary = gamelibrary;
    }

    private void descargarJuego(Juego juego) {
        try {
            List<Juego> juegosEnBiblioteca = userManager.listGames(loggedUser);
            for (Juego j : juegosEnBiblioteca) {
                if (j.getNombre().equalsIgnoreCase(juego.getNombre())) {
                    JOptionPane.showMessageDialog(this, "Este juego ya está en tu biblioteca.");
                    return;
                }
            }

            userManager.addGame(loggedUser, juego);
            JOptionPane.showMessageDialog(this, "¡Juego descargado exitosamente!");

            if (gamelibrary != null) {
                gamelibrary.cargarBiblioteca(); // Actualizar la biblioteca
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al descargar el juego: " + e.getMessage());
        }
    }

    private GameLibrary gamelibrary;
}
