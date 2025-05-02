import javax.swing.*;

public class CalorieTrackerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CalorieCount Chronicles");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Home", new HomePanel());
            tabbedPane.addTab("Weight Tracker", new WeightTrackerPanel());
            tabbedPane.addTab("Log Panel", new LogPanel());
            tabbedPane.addTab("Diet Plan", new DietPlanPanel());
            tabbedPane.addTab("Water Reminder", new WaterReminderPanel());
            tabbedPane.addTab("Required Calories", new RequiredCaloriePanel());
            tabbedPane.addTab("BMI Calculator", new BMICalculatorPanel());
            tabbedPane.addTab("FoodEntryPanel", new FoodEntryPanel());
            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}
