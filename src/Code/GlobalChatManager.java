package Code;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

public class GlobalChatManager {
    private static final String ChatFile = "Datos/chatGlobal.dat";
    private ArrayList<String> messages;

    public GlobalChatManager() {
        // Crear el archivo si no existe
        File chatFile = new File(ChatFile);
        if (!chatFile.exists()) {
            try {
                chatFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo de chat: " + e.getMessage());
            }
        }

        messages = loadMessages();
    }

    private ArrayList<String> loadMessages() {
        ArrayList<String> loadedMessages = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ChatFile))) {
            loadedMessages = (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de chat no encontrado. Iniciando nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar mensajes: " + e.getMessage());
        }
        return loadedMessages;
    }

    private void saveMessages() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ChatFile))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            System.out.println("Error al guardar mensajes: " + e.getMessage());
        }
    }

    public void addMessage(String username, String message) {
        String timestamp = new SimpleDateFormat("(dd/MM/yy hh:mm a)").format(new Date());
        String formattedMessage = timestamp + username + ": " + message;
        messages.add(formattedMessage);
        saveMessages();
    }

    public String getAllMessages() {
        StringBuilder chatContent = new StringBuilder();
        for (String message : messages) {
            chatContent.append(message).append("\n");
        }
        return chatContent.toString();
    }
}