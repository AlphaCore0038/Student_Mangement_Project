import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel totalStudentsValue;
    private JLabel averageMarksValue;
    private JLabel topperValue;

    public StatisticsPanel() {
        setLayout(new GridLayout(1, 3, 16, 0));
        setBackground(new Color(15, 23, 42));

        totalStudentsValue = new JLabel("0", SwingConstants.LEFT);
        averageMarksValue = new JLabel("0.00", SwingConstants.LEFT);
        topperValue = new JLabel("N/A", SwingConstants.LEFT);

        add(createCard("Total Students", totalStudentsValue, new Color(59, 130, 246)));
        add(createCard("Average Marks", averageMarksValue, new Color(16, 185, 129)));
        add(createCard("Topper", topperValue, new Color(245, 158, 11)));
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 8));
        card.setBackground(new Color(30, 41, 59));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setForeground(new Color(148, 163, 184));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public void updateStatistics(StudentManager manager) {
        totalStudentsValue.setText(String.valueOf(manager.getTotalStudents()));
        if (manager.getTotalStudents() == 0) {
            averageMarksValue.setText("N/A");
            topperValue.setText("N/A");
        } else {
            averageMarksValue.setText(String.format("%.2f", manager.getAverageMarks()));
            Student topper = manager.getTopper();
            if (topper != null) {
                topperValue.setText(topper.getStudentName() + " (" + String.format("%.1f", topper.getMarks()) + ")");
            } else {
                topperValue.setText("N/A");
            }
        }
    }
}
//...
