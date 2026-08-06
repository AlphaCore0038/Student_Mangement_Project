import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("OptionPane.background", new Color(32, 32, 32));
            UIManager.put("Panel.background", new Color(32, 32, 32));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", new Color(0, 102, 204));
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            StudentManager manager = new StudentManager();

            if (manager.getStudents().isEmpty()) {
                manager.addStudent(new Student(101, "Aarav Sharma", 88.5, "Computer Science", 4, "A", 92.0, "A", 60000.0, 60000.0));
                manager.addStudent(new Student(102, "Priya Patel", 94.0, "Information Tech", 4, "B", 96.5, "A+", 60000.0, 45000.0));
                manager.addStudent(new Student(103, "Rohan Verma", 76.25, "Electronics", 2, "A", 85.0, "B", 55000.0, 20000.0));
                manager.addStudent(new Student(104, "Ananya Iyer", 91.0, "Computer Science", 6, "A", 94.0, "A+", 65000.0, 65000.0));
                manager.addStudent(new Student(105, "Aditya Kumar", 82.5, "Mechanical", 4, "C", 88.0, "A", 50000.0, 30000.0));
                manager.addStudent(new Student(106, "Kavya Reddy", 68.0, "Civil", 2, "B", 79.5, "C", 48000.0, 48000.0));
                manager.addStudent(new Student(107, "Vikram Singh", 58.5, "Electrical", 6, "A", 72.0, "D", 52000.0, 25000.0));
            }

            MainDashboard dashboard = new MainDashboard(manager);
            dashboard.setVisible(true);
        });
    }
}
