package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import Code.DataManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends JPanel {

    CardLayout cardlayout;
    JPanel cardpanel;
    DataManager userManager;

    public Login(CardLayout cardlayout, JPanel cardpanel, DataManager userManager) {
        this.cardlayout = cardlayout;
        this.cardpanel = cardpanel;
        this.userManager = userManager;

        setLayout(null);
        setBackground(Color.BLACK);

        int panelWidth = 600;
        int panelHeight = 400;

        JLabel logo = new JLabel("LOGO", SwingConstants.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setBounds(panelWidth - 225, 100, 150, 150);
        logo.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        logo.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel usernameLabel = new JLabel("Usuario");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(50, 80, 200, 30);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(50, 110, 250, 30);
        usernameField.setForeground(Color.WHITE);
        usernameField.setBackground(Color.BLACK);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Password label and field
        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 160, 200, 30);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 190, 250, 30);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(Color.BLACK);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JButton loginButton = new JButton("Iniciar sesion");
        loginButton.setBounds(50, 280, 250, 40);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBorder(BorderFactory.createEmptyBorder());

        JLabel haveAccountLabel = new JLabel("¿Tienes cuenta?");
        haveAccountLabel.setForeground(Color.WHITE);
        haveAccountLabel.setBounds(50, 340, 120, 20);

        JButton registerButton = new JButton("REGÍSTRATE");
        registerButton.setBounds(180, 340, 120, 20);
        registerButton.setForeground(Color.BLACK);
        registerButton.setBackground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        add(logo);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(haveAccountLabel);
        add(registerButton);
        
        registerButton.addActionListener(e -> cardlayout.show(cardpanel, "register"));
        

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().isBlank() || passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(null, "Rellene todos los Campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                try {
                    if (userManager.login(username, password)) {
                        boolean isAdmin = userManager.isAdmin(username); // Verifica si es administrador
                        JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);

                        SwingUtilities.invokeLater(() -> {
                            Steam steamFrame = null;
                            try {
                                steamFrame = new Steam(username, isAdmin);
                            } catch (IOException ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            steamFrame.setVisible(true);
                        });

                      
                        ((JFrame) SwingUtilities.getWindowAncestor(loginButton)).dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al intentar iniciar sesión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
