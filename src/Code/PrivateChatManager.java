package Code;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PrivateChatManager {
    private static final String ChatsFolder = "Datos/Usuarios/";

    // Constructor: asegura que exista el directorio base
    public PrivateChatManager() {
        // Aseguramos que la carpeta base para los chats exista
        File chatDir = new File(ChatsFolder + "chat");
        if (!chatDir.exists()) {
            chatDir.mkdirs();  // Crear el directorio si no existe
        }
    }

    // Obtener el archivo correspondiente al chat privado
    private File getChatFile(String user1, String user2) {
        // Ordenamos los nombres para que el archivo sea único entre ambos usuarios
        String filename = user1.compareTo(user2) < 0 
            ? user1 + "_" + user2 + ".dat" 
            : user2 + "_" + user1 + ".dat";
        return new File(ChatsFolder + "chat/" + filename);
    }

    // Guardar un mensaje en el chat privado
    public void addMessage(String sender, String recipient, String message) {
        File chatFile = getChatFile(sender, recipient);
        ArrayList<String> messages = loadMessages(sender, recipient);

        String timestamp = new SimpleDateFormat("(dd/MM/yy hh:mm a)").format(new Date());
        String formattedMessage = timestamp + sender + ": " + message;

        messages.add(formattedMessage);
        saveMessages(chatFile, messages);
    }

    // Cargar mensajes de un chat privado
    public ArrayList<String> loadMessages(String user1, String user2) {
        File chatFile = getChatFile(user1, user2);
        ArrayList<String> messages = new ArrayList<>();
        if (chatFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chatFile))) {
                messages = (ArrayList<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar mensajes: " + e.getMessage());
            }
        }
        return messages;
    }

    // Guardar mensajes en el archivo binario
    private void saveMessages(File chatFile, ArrayList<String> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chatFile))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            System.out.println("Error al guardar mensajes: " + e.getMessage());
        }
    }

    // Obtener todos los mensajes como texto para mostrar
    public String getAllMessages(String user1, String user2) {
        ArrayList<String> messages = loadMessages(user1, user2);
        StringBuilder chatContent = new StringBuilder();
        for (String message : messages) {
            chatContent.append(message).append("\n");
        }
        return chatContent.toString();
    }
}