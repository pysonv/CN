import java.io.*;
import java.net.*;

public class HttpClient {

    public static void downloadWebpage(String hostname, String path) {
        Socket socket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        
        try {
            // Create a socket and connect to google.com (port 80 for HTTP)
            socket = new Socket(hostname, 80);
            
            // Create a writer to send the HTTP GET request
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            // Construct the GET request
            String request = "GET " + path + " HTTP/1.1\r\n" +
                             "Host: " + hostname + "\r\n" +
                             "Connection: close\r\n" +
                             "\r\n";
            
            // Send the GET request
            writer.write(request);
            writer.flush();
            
            // Create a reader to read the server's response
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Read the HTTP response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            
            // Split headers and body
            String[] responseParts = response.toString().split("\r\n\r\n", 2);
            String headers = responseParts[0];
            String body = responseParts.length > 1 ? responseParts[1] : "";
            
            // Print the HTTP headers and body (HTML content)
            System.out.println("HTTP Headers:\n");
            System.out.println(headers);
            System.out.println("\nWebpage Content:\n");
            System.out.println(body);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (writer != null) writer.close();
                if (reader != null) reader.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Example usage: Download the homepage of google.com
        String hostname = "www.google.com";
        String path = "/"; // Root path (homepage)
        downloadWebpage(hostname, path);
    }
}

