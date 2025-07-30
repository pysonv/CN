package PacketCorruptionAnalyzer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

class Packet {
    int id;
    String data;
    String checksum;

    public Packet(int id, String data) {
        this.id = id;
        this.data = data;
        this.checksum = generateChecksum(data);
    }

    public static String generateChecksum(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isCorrupted() {
        return !this.checksum.equals(generateChecksum(this.data));
    }
}

public class PacketCorruptionAnalyzer extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ArrayList<Packet> transmittedPackets = new ArrayList<>();
    private int totalCorrupted = 0;

    public PacketCorruptionAnalyzer() {
        setTitle("📦 Packet Corruption Analyzer with Visualization");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // GUI Containers
        JPanel packetPanel = new JPanel();
        packetPanel.setLayout(new BoxLayout(packetPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(packetPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Packet Details"));
        scrollPane.setPreferredSize(new Dimension(450, 600)); // fixed size for nice scrolling

        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel chartPanelWrapper = new JPanel(new BorderLayout());

        JLabel totalLabel = new JLabel();
        JLabel corruptedLabel = new JLabel();
        JLabel okLabel = new JLabel();

        // Colors
        Color green = new Color(34, 139, 34);
        Color red = Color.RED;

        // Step 1: Create packets
        ArrayList<Packet> originalPackets = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            originalPackets.add(new Packet(i, "data_packet_" + i));
        }

        // Step 2: Transmit and corrupt packets randomly (30% chance)
        Random rand = new Random();
        for (Packet pkt : originalPackets) {
            if (rand.nextDouble() < 0.3) {
                String corruptedData = new StringBuilder(pkt.data).reverse().toString();
                Packet corruptedPkt = new Packet(pkt.id, corruptedData);
                corruptedPkt.checksum = pkt.checksum; // simulate corruption by reusing original checksum
                transmittedPackets.add(corruptedPkt);
                totalCorrupted++;
            } else {
                transmittedPackets.add(pkt);
            }
        }

        // Print packet details to console
        System.out.println("Packet Transmission Summary:");
        for (Packet pkt : transmittedPackets) {
            boolean isCorrupted = pkt.isCorrupted();
            String status = isCorrupted ? "❌ Corrupted" : "✅ OK";
            System.out.printf("ID: %02d | Data: %-15s | Checksum: %s | Status: %s%n",
                    pkt.id, pkt.data, pkt.checksum.substring(0, 12) + "...", status);
        }
        System.out.println("-------------------------------------------------------");

        // Step 3: Display packets in packetPanel
        for (Packet pkt : transmittedPackets) {
            boolean isCorrupted = pkt.isCorrupted();
            String status = isCorrupted ? "❌ Corrupted" : "✅ OK";
            JLabel label = new JLabel(String.format("ID: %02d | Data: %-15s | Checksum: %s | Status: %s",
                    pkt.id, pkt.data, pkt.checksum.substring(0, 12) + "...", status));
            label.setOpaque(true);
            label.setFont(new Font("Courier New", Font.PLAIN, 14));
            label.setForeground(Color.WHITE);
            label.setBackground(isCorrupted ? red : green);
            label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            packetPanel.add(label);
        }

        // Step 4: Stats Summary
        int total = transmittedPackets.size();
        int ok = total - totalCorrupted;

        totalLabel.setText("📦 Total Packets: " + total);
        okLabel.setText("✅ OK: " + ok);
        corruptedLabel.setText("❌ Corrupted: " + totalCorrupted);

        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        okLabel.setFont(new Font("Arial", Font.BOLD, 14));
        corruptedLabel.setFont(new Font("Arial", Font.BOLD, 14));

        summaryPanel.add(totalLabel);
        summaryPanel.add(okLabel);
        summaryPanel.add(corruptedLabel);

        // Step 5: Legend
        JLabel greenLabel = new JLabel("✅ OK");
        greenLabel.setOpaque(true);
        greenLabel.setBackground(green);
        greenLabel.setForeground(Color.WHITE);
        greenLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel redLabel = new JLabel("❌ Corrupted");
        redLabel.setOpaque(true);
        redLabel.setBackground(red);
        redLabel.setForeground(Color.WHITE);
        redLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        legendPanel.add(greenLabel);
        legendPanel.add(redLabel);

        // Step 6: Bar Chart (JFreeChart)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(ok, "Packets", "OK");
        dataset.addValue(totalCorrupted, "Packets", "Corrupted");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Packet Corruption Summary", "Status", "Count",
                dataset);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(450, 300));
        chartPanelWrapper.add(chartPanel, BorderLayout.CENTER);

        // Step 7: Progress Bar
        int corruptionRate = (int) (((double) totalCorrupted / total) * 100);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(corruptionRate);
        progressBar.setString("Corruption Rate: " + corruptionRate + "%");
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.RED);
        progressBar.setPreferredSize(new Dimension(600, 25));

        // Add all to statsPanel
        statsPanel.add(summaryPanel);
        statsPanel.add(legendPanel);
        statsPanel.add(progressBar);

        // Add panels to frame
        add(statsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.WEST);
        add(chartPanelWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PacketCorruptionAnalyzer::new);
    }
}
