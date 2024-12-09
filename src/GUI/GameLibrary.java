package GUI;

import Code.Juego;
import Code.DataManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GameLibrary extends JPanel {

    private DataManager userManager;
    private String loggedUser;

    public GameLibrary(DataManager userManager, String loggedUser) {
        this.userManager = userManager;
        this.loggedUser = loggedUser;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        cargarBiblioteca();
    }

    void cargarBiblioteca() {
        removeAll();
        try {
            List<Juego> juegos = userManager.listGames(loggedUser);

            if (juegos.isEmpty()) {
                JLabel emptyLabel = new JLabel("No hay juegos en tu biblioteca.");
                emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
                add(emptyLabel);
            } else {
                for (Juego juego : juegos) {
                    JPanel juegoPanel = crearPanelJuego(juego);
                    add(juegoPanel);
                    add(Box.createVerticalStrut(10)); // Espacio entre juegos
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la biblioteca: " + e.getMessage());
        }
        revalidate();
        repaint();
    }

    private JPanel crearPanelJuego(Juego juego) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setMaximumSize(new Dimension(600, 200));

        // Imagen del juego
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

        // Botón para eliminar el juego
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> eliminarJuego(juego));
        panel.add(deleteButton, BorderLayout.EAST);

        return panel;
    }

    private void eliminarJuego(Juego juego) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar este juego de tu biblioteca?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                
                List<Juego> juegos = userManager.listGames(loggedUser);
                juegos.removeIf(j -> j.getNombre().equals(juego.getNombre()));

                userManager.clearUserGames(loggedUser);
                for (Juego j : juegos) {
                    userManager.addGame(loggedUser, j);
                }

                JOptionPane.showMessageDialog(this, "Juego eliminado correctamente.");
                cargarBiblioteca();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el juego: " + e.getMessage());
            }
        }
    }
    
    
}