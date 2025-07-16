import java.io.*;
import java.net.*;

public class SocketHTTPClient {
    public static void main(String[] args) {zX        try (
            Socket socket = new Socket("www.martinbroadhurst.com", 80);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Send an HTTP GET request
            out.println("GET / HTTP/1.1");
            out.println("Host: www.martinbroadhurst.com");
            out.println(); 
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
