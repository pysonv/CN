package ServerClient;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        System.out.println("Connecting to server...");

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to server!");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from the server
            new Thread(() -> {
                String serverMsg;
                try {
                    while ((serverMsg = input.readLine()) != null) {
                        System.out.println("Server: " + serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Server disconnected.");
                }
            }).start();

            // Main thread: send messages from client to server
            String clientMsg;
            while ((clientMsg = consoleInput.readLine()) != null) {
                output.println(clientMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
