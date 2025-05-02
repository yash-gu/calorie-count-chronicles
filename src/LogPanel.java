import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LogPanel extends JPanel {
    private JComboBox<String> foodDropdown;
    private JComboBox<String> mealTypeCombo;
    private JTextField servingsField;
    private JTextArea logArea;
    private JLabel totalCalorieLabel;

    public LogPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel logInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logInputPanel.setBackground(new Color(240, 255, 240));

        foodDropdown = new JComboBox<>();
        servingsField = new JTextField(5);
        mealTypeCombo = new JComboBox<>(new String[] {"Breakfast", "Lunch", "Dinner", "Snacks"});
        JButton logButton = new JButton("Log Calories");

        logInputPanel.add(new JLabel("Food:"));
        logInputPanel.add(foodDropdown);
        logInputPanel.add(new JLabel("Servings:"));
        logInputPanel.add(servingsField);
        logInputPanel.add(new JLabel("Meal:"));
        logInputPanel.add(mealTypeCombo);
        logInputPanel.add(logButton);

        add(logInputPanel, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        logArea.setEditable(false);
        logArea.setBackground(new Color(230, 230, 230));
        logArea.setForeground(new Color(20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log History"));
        add(scrollPane, BorderLayout.CENTER);

        totalCalorieLabel = new JLabel("Total Calories: 0 kcal");
        totalCalorieLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalCalorieLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(totalCalorieLabel, BorderLayout.SOUTH);

        logButton.addActionListener(e -> logCalories());
        loadFoodDropdown();
        loadLogs();
    }

    private void loadFoodDropdown() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM food_items")) {
            foodDropdown.removeAllItems();
            while (rs.next()) {
                foodDropdown.addItem(rs.getInt("id") + ": " + rs.getString("food_name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load food list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logCalories() {
        String selected = (String) foodDropdown.getSelectedItem();
        if (selected == null || servingsField.getText().isEmpty()) return;

        int foodId = Integer.parseInt(selected.split(":")[0]);
        int servings;
        try {
            servings = Integer.parseInt(servingsField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number for servings.");
            return;
        }

        String mealType = (String) mealTypeCombo.getSelectedItem();
        int calPerServing = 0;

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement calStmt = conn.prepareStatement("SELECT calories_per_serving FROM food_items WHERE id = ?");
            calStmt.setInt(1, foodId);
            ResultSet rs = calStmt.executeQuery();
            if (rs.next()) {
                calPerServing = rs.getInt(1);
            }

            int totalCalories = calPerServing * servings;

            PreparedStatement insertLog = conn.prepareStatement(
                    "INSERT INTO user_log (food_id, servings, total_calories, meal_type) VALUES (?, ?, ?, ?)");
            insertLog.setInt(1, foodId);
            insertLog.setInt(2, servings);
            insertLog.setInt(3, totalCalories);
            insertLog.setString(4, mealType != null ? mealType : "Unknown");
            insertLog.executeUpdate();

            servingsField.setText("");
            mealTypeCombo.setSelectedIndex(0);
            loadLogs();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error logging calories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLogs() {
        logArea.setText("");
        int grandTotalCalories = 0;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT DATE(u.log_time) AS log_date, u.log_time, f.food_name, u.servings, " +
                             "u.total_calories, u.meal_type " +
                             "FROM user_log u JOIN food_items f ON u.food_id = f.id " +
                             "ORDER BY log_date DESC, u.log_time DESC")) {

            String currentDate = "";
            int dailyTotal = 0;
            String currentMeal = "";

            while (rs.next()) {
                String logDate = rs.getDate("log_date").toString();
                String mealType = rs.getString("meal_type");

                if (!logDate.equals(currentDate)) {
                    if (!currentDate.isEmpty()) {
                        logArea.append(String.format("  ➤ Total for %s: %d kcal\n\n", currentDate, dailyTotal));
                        dailyTotal = 0;
                    }
                    currentDate = logDate;
                    currentMeal = "";
                    logArea.append("📅 " + currentDate + "\n");
                }

                if (mealType != null && !mealType.equals(currentMeal)) {
                    currentMeal = mealType;
                    logArea.append(String.format("  [%s]\n", currentMeal.toUpperCase()));
                }

                int cals = rs.getInt("total_calories");
                dailyTotal += cals;
                grandTotalCalories += cals;

                logArea.append(String.format("    %s - %d serving(s) of %s: %d kcal\n",
                        rs.getTimestamp("log_time").toLocalDateTime().toLocalTime(),
                        rs.getInt("servings"),
                        rs.getString("food_name"),
                        cals));
            }

            if (!currentDate.isEmpty()) {
                logArea.append(String.format("  ➤ Total for %s: %d kcal\n\n", currentDate, dailyTotal));
            }

            totalCalorieLabel.setText("Total Calories: " + grandTotalCalories + " kcal");

        } catch (Exception e) {
            logArea.setText("Failed to load logs: " + e.getMessage());
            totalCalorieLabel.setText("Total Calories: 0 kcal");
        }
    }
}
