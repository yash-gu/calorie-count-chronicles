import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class WaterReminderPanel extends JPanel {
    private JTextField intervalField;
    private JButton setReminderButton;
    private JLabel statusLabel;
    private Timer timer;
    private int reminderInterval; // In minutes

    public WaterReminderPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(34, 45, 65));

        // Input Panel for reminder interval
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(44, 62, 80));
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel intervalLabel = new JLabel("Set Reminder Interval (in minutes):");
        intervalLabel.setForeground(Color.WHITE);

        intervalField = new JTextField(5);
        setReminderButton = new JButton("Set Reminder");

        inputPanel.add(intervalLabel);
        inputPanel.add(intervalField);
        inputPanel.add(setReminderButton);

        // Status Label
        statusLabel = new JLabel("Reminder Status: Not Set");
        statusLabel.setForeground(Color.WHITE);

        // Add components to the main panel
        add(inputPanel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.CENTER);

        // Action listener for the button
        setReminderButton.addActionListener(e -> setReminder());
    }

    private void setReminder() {
        try {
            reminderInterval = Integer.parseInt(intervalField.getText().trim());

            if (reminderInterval <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for the interval.");
                return;
            }

            // Stop any existing reminder
            if (timer != null) {
                timer.cancel();
            }

            // Start a new timer
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    showWaterReminder();
                }
            }, 0, reminderInterval * 60 * 1000); // Convert minutes to milliseconds

            statusLabel.setText("Reminder Set: Every " + reminderInterval + " minutes.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the interval.");
        }
    }

    private void showWaterReminder() {
        SwingUtilities.invokeLater(() -> {
            // Show pop-up notification to remind the user to drink water
            JOptionPane.showMessageDialog(this, "Reminder: Drink Water!", "Water Reminder", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
