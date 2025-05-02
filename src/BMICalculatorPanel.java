import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BMICalculatorPanel extends JPanel {
    private JTextField weightField;
    private JTextField heightField;
    private JLabel resultLabel;

    public BMICalculatorPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Bigger gaps
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Custom Font
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        JLabel titleLabel = new JLabel("BMI Calculator");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(50, 50, 150));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setFont(labelFont);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(weightLabel, gbc);

        weightField = new JTextField(10);
        weightField.setFont(fieldFont);
        weightField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        gbc.gridx = 1;
        add(weightField, gbc);

        JLabel heightLabel = new JLabel("Height (cm):");
        heightLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(heightLabel, gbc);

        heightField = new JTextField(10);
        heightField.setFont(fieldFont);
        heightField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        gbc.gridx = 1;
        add(heightField, gbc);

        JButton calculateButton = new JButton("Calculate BMI");
        calculateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        calculateButton.setBackground(new Color(100, 149, 237));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(calculateButton, gbc);

        resultLabel = new JLabel("Your BMI will appear here.");
        resultLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        resultLabel.setForeground(new Color(70, 70, 70));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        add(resultLabel, gbc);

        // Button Action
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateBMI();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Gradient background
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(240, 248, 255);
        Color color2 = new Color(200, 220, 255);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void calculateBMI() {
        try {
            double weight = Double.parseDouble(weightField.getText());
            double heightCm = Double.parseDouble(heightField.getText());
            double heightM = heightCm / 100.0;
            double bmi = weight / (heightM * heightM);

            resultLabel.setText(String.format("Your BMI is: %.2f", bmi));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for weight and height.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
