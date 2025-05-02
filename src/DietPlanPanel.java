import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DietPlanPanel extends JPanel {
    private JTextField calorieField;
    private JTextArea dietPlanArea;
    private JButton generateDietButton;

    public DietPlanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(34, 45, 65));

        // Input Panel for calories
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(44, 62, 80));
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel calorieLabel = new JLabel("Enter Daily Calorie Intake:");
        calorieLabel.setForeground(Color.WHITE);
        calorieField = new JTextField(10);

        generateDietButton = new JButton("Generate Diet Plan");

        inputPanel.add(calorieLabel);
        inputPanel.add(calorieField);
        inputPanel.add(generateDietButton);

        // Diet Plan Area
        dietPlanArea = new JTextArea(10, 40);
        dietPlanArea.setEditable(false);
        dietPlanArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dietPlanArea.setForeground(Color.WHITE);
        dietPlanArea.setBackground(new Color(52, 73, 94));
        JScrollPane dietPlanScrollPane = new JScrollPane(dietPlanArea);

        // Add components to the main panel
        add(inputPanel, BorderLayout.NORTH);
        add(dietPlanScrollPane, BorderLayout.CENTER);

        // Add ActionListener for the button
        generateDietButton.addActionListener(e -> generateDietPlan());
    }

    private void generateDietPlan() {
        try {
            double dailyCalories = Double.parseDouble(calorieField.getText().trim());

            if (dailyCalories <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for daily calories.");
                return;
            }

            // Divide the daily calories into meal types (just an example)
            double breakfastCalories = dailyCalories * 0.25;
            double lunchCalories = dailyCalories * 0.35;
            double dinnerCalories = dailyCalories * 0.30;
            double snackCalories = dailyCalories * 0.10;

            // Display the diet plan
            dietPlanArea.setText(String.format("Your Customized Diet Plan:\n\n" +
                            "Breakfast: %.2f kcal\nLunch: %.2f kcal\nDinner: %.2f kcal\nSnacks: %.2f kcal\n\n",
                    breakfastCalories, lunchCalories, dinnerCalories, snackCalories));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for daily calories.");
        }
    }
}
