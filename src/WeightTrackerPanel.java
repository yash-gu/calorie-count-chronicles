import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

public class WeightTrackerPanel extends JPanel {
    private JTextField weightField;
    private JTextArea logArea;
    private XChartPanel<XYChart> chartPanel;

    public WeightTrackerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for input
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Enter Weight (kg):"));
        weightField = new JTextField(5);
        inputPanel.add(weightField);
        JButton addButton = new JButton("Add");
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // Log area
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Weight Log"));
        add(logScrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(this::addWeightEntry);

        // Load existing logs and display chart
        loadLogs();
        displayGraph();
    }

    private void addWeightEntry(ActionEvent e) {
        String weightText = weightField.getText().trim();
        if (weightText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a weight.");
            return;
        }

        try {
            double weight = Double.parseDouble(weightText);
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO weight_log (log_date, weight) VALUES (?, ?)");
                stmt.setDate(1, currentDate);
                stmt.setDouble(2, weight);
                stmt.executeUpdate();
            }

            weightField.setText("");
            loadLogs();
            displayGraph();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid weight entered.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void loadLogs() {
        logArea.setText("");
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM weight_log ORDER BY log_date DESC")) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while (rs.next()) {
                java.util.Date date = rs.getDate("log_date");
                double weight = rs.getDouble("weight");
                logArea.append(String.format("📅 %s - %.2f kg\n", sdf.format(date), weight));
            }

        } catch (SQLException ex) {
            logArea.setText("Failed to load weight logs.");
        }
    }

    private void displayGraph() {
        List<java.util.Date> dates = new ArrayList<>();
        List<Double> weights = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT log_date, weight FROM weight_log ORDER BY log_date ASC")) {

            while (rs.next()) {
                dates.add(rs.getDate("log_date"));
                weights.add(rs.getDouble("weight"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading graph data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dates.isEmpty() || weights.isEmpty()) {
            if (chartPanel != null) {
                remove(chartPanel);
                chartPanel = null;
            }
            revalidate();
            repaint();
            return;
        }

        XYChart chart = new XYChartBuilder()
                .width(700).height(400)
                .title("Weight Over Time")
                .xAxisTitle("Date")
                .yAxisTitle("Weight (kg)")
                .build();

        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setDatePattern("dd-MM-yyyy");
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setMarkerSize(6);
        chart.addSeries("Weight", dates, weights);

        if (chartPanel != null) {
            remove(chartPanel);
        }
        chartPanel = new XChartPanel<>(chart);
        add(chartPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
