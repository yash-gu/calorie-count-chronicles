import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Welcome to Calorie Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel subtitle = new JLabel("Track your meals and monitor your daily calorie intake.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        ImageIcon icon = new ImageIcon("/Users/yashgupta/IdeaProjects/calorie_tracker/src/images/5.jpg"); // Replace with your own banner image
        JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);

        add(title, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(subtitle, BorderLayout.SOUTH);
    }
}
