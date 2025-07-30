import java.net.*;

public class DNSSimulation {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("localhost");
        byte[] sendData = "www.google.com".getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, 9876);
        socket.send(sendPacket);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        String response = new String(receivePacket.getData()).trim();
        System.out.println("DNS Resolved IP: " + response);
        socket.close();
    }
}
