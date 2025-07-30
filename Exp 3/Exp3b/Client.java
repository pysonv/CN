package ServerClient;

import java.io.*;
import java.net.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Thread to read messages from server
            Thread readThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            readThread.start();

            // Read user input and send to server
            String inputLine;
            while ((inputLine = userInput.readLine()) != null) {
                out.println(inputLine);
                if ("exit".equalsIgnoreCase(inputLine)) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Unable to connect to server.");
            e.printStackTrace();
        }
    }
}

