package ServerClient;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;
        System.out.println("Server is running...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from the client
            new Thread(() -> {
                String clientMsg;
                try {
                    while ((clientMsg = input.readLine()) != null) {
                        System.out.println("Client: " + clientMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Client disconnected.");
                }
            }).start();

            // Main thread: send messages from server to client
            String serverMsg;
            while ((serverMsg = consoleInput.readLine()) != null) {
                output.println(serverMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
