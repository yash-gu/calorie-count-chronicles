import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.sql.*;

public class FoodEntryPanel extends JPanel {
    private JTextField foodNameField;
    private JTextField calorieField;
    private JTextField imagePathField;
    private JComboBox<String> mealTypeCombo;
    private JButton addButton;
    private JButton browseButton;
    private JLabel statusLabel;

    public FoodEntryPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createTitledBorder("🍽️ Add a New Food Item"));

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        foodNameField = new JTextField(20);
        foodNameField.setToolTipText("Enter the name of the food item");

        calorieField = new JTextField(20);
        calorieField.setToolTipText("Enter the number of calories per serving");

        imagePathField = new JTextField(15);
        imagePathField.setEditable(false);
        browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> chooseImageFile());

        mealTypeCombo = new JComboBox<>(new String[]{"Breakfast", "Lunch", "Dinner", "Snacks", "Unknown"});

        addButton = new JButton("➕ Add Food");
        addButton.setBackground(new Color(72, 201, 176));
        addButton.setForeground(Color.WHITE);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.BLUE);

        // Add components to formPanel
        formPanel.add(labeledPanel("🥗 Food Name:", foodNameField));
        formPanel.add(labeledPanel("🔥 Calories:", calorieField));
        formPanel.add(imageInputPanel());
        formPanel.add(labeledPanel("🍴 Meal Type:", mealTypeCombo));
        formPanel.add(addButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(statusLabel);

        add(formPanel, BorderLayout.CENTER);

        // Action
        addButton.addActionListener(e -> insertFoodItem());
    }

    private JPanel labeledPanel(String label, JComponent input) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(input);
        return panel;
    }

    private JPanel imageInputPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("🖼️ Image:"));
        panel.add(imagePathField);
        panel.add(browseButton);
        return panel;
    }

    private void chooseImageFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Image File");
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imagePathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void insertFoodItem() {
        String name = foodNameField.getText().trim();
        String calStr = calorieField.getText().trim();
        String imagePath = imagePathField.getText().trim();
        String mealType = (String) mealTypeCombo.getSelectedItem();

        if (name.isEmpty() || calStr.isEmpty()) {
            statusLabel.setText("❌ Please enter both food name and calorie amount.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        int calories;
        try {
            calories = Integer.parseInt(calStr);
        } catch (NumberFormatException e) {
            statusLabel.setText("❌ Calories must be a valid number.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO food_items (food_name, calories_per_serving, image_path, meal_type) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, calories);
            stmt.setString(3, imagePath.isEmpty() ? null : imagePath);
            stmt.setString(4, mealType);
            stmt.executeUpdate();

            statusLabel.setText("✅ Food item added successfully!");
            statusLabel.setForeground(new Color(46, 204, 113));

            // Reset fields
            foodNameField.setText("");
            calorieField.setText("");
            imagePathField.setText("");
            mealTypeCombo.setSelectedIndex(0);
        } catch (Exception e) {
            statusLabel.setText("❌ Error: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }
}

