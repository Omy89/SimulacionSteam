package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import Code.DataManager;

public class Register extends JPanel {

    CardLayout cardlayout;
    JPanel cardpanel;
    DataManager userManager;

    public Register(CardLayout cardlayout, JPanel cardpanel, DataManager userManager) {
        this.cardlayout = cardlayout;
        this.cardpanel = cardpanel;
        this.userManager = userManager;

        setLayout(null);
        setBackground(Color.BLACK);

        int panelWidth = 600;
        int panelHeight = 400;

        // Logo
        JLabel logo = new JLabel("REGISTRO", SwingConstants.CENTER);
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

        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 160, 200, 30);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 190, 250, 30);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(Color.BLACK);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        boolean[] isAdmin = {false};
        JButton toggleAdminButton = new JButton("Usuario Normal");
        toggleAdminButton.setBounds(50, 240, 250, 40);
        toggleAdminButton.setForeground(Color.BLACK);
        toggleAdminButton.setBackground(Color.WHITE);
        toggleAdminButton.setFocusPainted(false);
        toggleAdminButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        toggleAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isAdmin[0] = !isAdmin[0];
                toggleAdminButton.setText(isAdmin[0] ? "Administrador" : "Usuario Normal");
            }
        });

        JButton registerButton = new JButton("Registrar");
        registerButton.setBounds(50, 300, 250, 40);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBorder(BorderFactory.createEmptyBorder());

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Rellene todos los Campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                try {
                    if (userManager.register(username, password, isAdmin[0])) {
                        JOptionPane.showMessageDialog(null, "Registro exitoso!");
                        passwordField.setText("");
                        usernameField.setText("");
                        toggleAdminButton.setText("Usuario Normal");
                        isAdmin[0] = false;
                        cardlayout.show(cardpanel, "login");
                    } else {
                        JOptionPane.showMessageDialog(null, "Lo sentimos mucho, ya existe un usuario con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error durante el registro", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // "¿Ya tienes cuenta? Inicia sesión" section
        JLabel haveAccountLabel = new JLabel("¿Ya tienes cuenta?");
        haveAccountLabel.setForeground(Color.WHITE);
        haveAccountLabel.setBounds(50, 360, 120, 20);

        JButton backButton = new JButton("Inicia sesión");
        backButton.setBounds(180, 360, 120, 20);
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.setText("");
                usernameField.setText("");
                toggleAdminButton.setText("Usuario Normal");
                isAdmin[0] = false;
                cardlayout.show(cardpanel, "login");
            }
        });

        add(logo);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(toggleAdminButton);
        add(registerButton);
        add(haveAccountLabel);
        add(backButton);
    }
}
