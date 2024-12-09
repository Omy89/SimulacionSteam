/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Code.PrivateChatManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PrivateChat extends JPanel {
    private PrivateChatManager chatManager;
    private String currentUser;
    private String recipient;
    private JTextArea chatArea;
    private JTextField messageField;

    public PrivateChat(PrivateChatManager chatManager, String currentUser, String recipient) {
        this.chatManager = chatManager;
        this.currentUser = currentUser;
        this.recipient = recipient;
        setLayout(new BorderLayout());

        // Área de texto para mostrar los mensajes
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setText(chatManager.getAllMessages(currentUser, recipient));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para enviar mensajes
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Enviar");

        // Enviar mensaje al hacer clic en el botón
        sendButton.addActionListener((ActionEvent e) -> {
            sendMessage();
        });

        // Enviar mensaje al presionar Enter
        messageField.addActionListener((ActionEvent e) -> {
            sendMessage();
        });

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }

    // Enviar mensaje
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            chatManager.addMessage(currentUser, recipient, message);
            chatArea.setText(chatManager.getAllMessages(currentUser, recipient));
            messageField.setText("");
        }
    }
}