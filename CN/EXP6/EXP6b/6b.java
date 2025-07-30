import java.util.*;

public class RARP {
    public static void main(String[] args) {
        Map<String, String> arpTable = new HashMap<>();
        arpTable.put("192.168.1.2", "AA:BB:CC:DD:EE:01");
        arpTable.put("192.168.1.3", "AA:BB:CC:DD:EE:02");
        arpTable.put("192.168.1.4", "AA:BB:CC:DD:EE:03");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter MAC address to resolve: ");
        String mac = sc.nextLine();

        String ipFound = null;
        for (Map.Entry<String, String> entry : arpTable.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(mac)) {
                ipFound = entry.getKey();
                break;
            }
        }

        if (ipFound != null) {
            System.out.println("IP Address: " + ipFound);
        } else {
            System.out.println("MAC address not found in table.");
        }

        sc.close();
    }
}
