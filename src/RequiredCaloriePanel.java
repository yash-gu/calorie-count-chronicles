import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RequiredCaloriePanel extends JPanel {
    private JTextField ageField, weightField, heightField;
    private JComboBox<String> genderCombo, activityLevelCombo;
    private JTextArea resultArea;
    private JButton calculateButton;

    public RequiredCaloriePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(34, 45, 65));

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(44, 62, 80));
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel ageLabel = new JLabel("Age:");
        JLabel weightLabel = new JLabel("Weight (kg):");
        JLabel heightLabel = new JLabel("Height (cm):");
        JLabel genderLabel = new JLabel("Gender:");
        JLabel activityLabel = new JLabel("Activity Level:");

        ageLabel.setForeground(Color.WHITE);
        weightLabel.setForeground(Color.WHITE);
        heightLabel.setForeground(Color.WHITE);
        genderLabel.setForeground(Color.WHITE);
        activityLabel.setForeground(Color.WHITE);

        ageField = new JTextField(5);
        weightField = new JTextField(5);
        heightField = new JTextField(5);

        genderCombo = new JComboBox<>(new String[] {"Male", "Female"});
        activityLevelCombo = new JComboBox<>(new String[] {
                "Sedentary (little or no exercise)",
                "Lightly active (light exercise/sports 1-3 days/week)",
                "Moderately active (moderate exercise/sports 3-5 days/week)",
                "Very active (hard exercise/sports 6-7 days a week)",
                "Super active (very hard exercise, physical job, or training)"
        });

        calculateButton = new JButton("Calculate Required Calories");

        inputPanel.add(ageLabel);
        inputPanel.add(ageField);
        inputPanel.add(weightLabel);
        inputPanel.add(weightField);
        inputPanel.add(heightLabel);
        inputPanel.add(heightField);
        inputPanel.add(genderLabel);
        inputPanel.add(genderCombo);
        inputPanel.add(activityLabel);
        inputPanel.add(activityLevelCombo);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(44, 62, 80));
        buttonPanel.add(calculateButton);

        // Result Area
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultArea.setForeground(Color.WHITE);
        resultArea.setBackground(new Color(52, 73, 94));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add components to the main panel
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Centering the button in the middle of the panel
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        buttonPanel.add(calculateButton, gbc);

        calculateButton.addActionListener(e -> calculateCalories());
    }

    private void calculateCalories() {
        try {
            // Get user inputs
            int age = Integer.parseInt(ageField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            String gender = (String) genderCombo.getSelectedItem();
            String activityLevel = (String) activityLevelCombo.getSelectedItem();

            // Validate inputs
            if (age <= 0 || weight <= 0 || height <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid values for age, weight, and height.");
                return;
            }

            // BMR Calculation (Mifflin-St Jeor Equation)
            double bmr;
            if (gender.equals("Male")) {
                bmr = 10 * weight + 6.25 * height - 5 * age + 5;
            } else {
                bmr = 10 * weight + 6.25 * height - 5 * age - 161;
            }

            // Adjust BMR based on activity level
            double activityMultiplier;
            switch (activityLevel) {
                case "Sedentary (little or no exercise)":
                    activityMultiplier = 1.2;
                    break;
                case "Lightly active (light exercise/sports 1-3 days/week)":
                    activityMultiplier = 1.375;
                    break;
                case "Moderately active (moderate exercise/sports 3-5 days/week)":
                    activityMultiplier = 1.55;
                    break;
                case "Very active (hard exercise/sports 6-7 days a week)":
                    activityMultiplier = 1.725;
                    break;
                case "Super active (very hard exercise, physical job, or training)":
                    activityMultiplier = 1.9;
                    break;
                default:
                    activityMultiplier = 1.2; // Default to sedentary
            }

            // Calculate required calories
            double requiredCalories = bmr * activityMultiplier;

            // Display result
            resultArea.setText(String.format("Required Daily Calories: %.2f kcal/day", requiredCalories));

            // Optionally disable inputs to prevent further changes
            ageField.setEnabled(false);
            weightField.setEnabled(false);
            heightField.setEnabled(false);
            genderCombo.setEnabled(false);
            activityLevelCombo.setEnabled(false);
            calculateButton.setEnabled(false);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for age, weight, and height.");
        }
    }
}
