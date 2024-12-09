
package GUI;

import Code.GlobalChatManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GlobalChat extends JPanel {
    private GlobalChatManager chatManager;
    private JTextArea chatArea;
    private JTextField messageField;

    public GlobalChat(GlobalChatManager chatManager, String username) {
        this.chatManager = chatManager;
        setLayout(new BorderLayout());

        // Área de texto para mostrar mensajes
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setText(chatManager.getAllMessages());
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para enviar mensajes
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Enviar");

        // Enviar mensaje al hacer clic en el botón
        sendButton.addActionListener((ActionEvent e) -> {
            sendMessage(username);
        });

        // Enviar mensaje al presionar Enter
        messageField.addActionListener((ActionEvent e) -> {
            sendMessage(username);
        });

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }

    // Enviar mensaje
    private void sendMessage(String username) {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            chatManager.addMessage(username, message);
            chatArea.setText(chatManager.getAllMessages());
            messageField.setText("");
        }
    }
}