Program:-
// ARP.java
import java.util.*;

public class ARP {
    public static void main(String[] args) {
        Map<String, String> arpTable = new HashMap<>();
        arpTable.put("192.168.1.2", "AA:BB:CC:DD:EE:01");
        arpTable.put("192.168.1.3", "AA:BB:CC:DD:EE:02");
        arpTable.put("192.168.1.4", "AA:BB:CC:DD:EE:03");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter IP address to resolve: ");
        String ip = sc.nextLine();

        if (arpTable.containsKey(ip)) {
            System.out.println("MAC Address: " + arpTable.get(ip));
        } else {
            System.out.println("IP address not found in ARP table.");
        }

        sc.close();
    }
}
