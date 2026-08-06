import javax.swing.*;
import java.awt.*;

public class GradesDialog extends JDialog {
    private JTextField searchIdField;
    private JButton searchButton;
    private JLabel marksLabel;
    private JLabel calcGradeLabel;
    private JComboBox<String> gradeComboBox;
    private JButton updateButton;
    private JButton cancelButton;
    private StudentManager manager;
    private MainDashboard dashboard;
    private Student currentStudent;

    public GradesDialog(MainDashboard parent, StudentManager manager) {
        super(parent, "Grades Management", true);
        this.dashboard = parent;
        this.manager = manager;

        setSize(440, 360);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(28, 28, 28));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Grades Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(createLabel("Student ID:"), gbc);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(28, 28, 28));
        searchIdField = createTextField();
        searchButton = createButton("Search", new Color(0, 102, 204));
        searchPanel.add(searchIdField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        gbc.gridx = 1;
        mainPanel.add(searchPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(createLabel("Current Marks:"), gbc);
        marksLabel = new JLabel("N/A");
        marksLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        marksLabel.setForeground(new Color(220, 220, 220));
        gbc.gridx = 1;
        mainPanel.add(marksLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(createLabel("Calculated Grade:"), gbc);
        calcGradeLabel = new JLabel("N/A");
        calcGradeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        calcGradeLabel.setForeground(new Color(220, 140, 40));
        gbc.gridx = 1;
        mainPanel.add(calcGradeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(createLabel("Grade (Override):"), gbc);

        String[] grades = {"A+", "A", "B", "C", "D", "F"};
        gradeComboBox = new JComboBox<>(grades);
        gradeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gradeComboBox.setBackground(new Color(40, 40, 40));
        gradeComboBox.setForeground(Color.WHITE);
        gradeComboBox.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(gradeComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(28, 28, 28));

        updateButton = createButton("Update Grade", new Color(0, 102, 204));
        updateButton.setEnabled(false);
        cancelButton = createButton("Cancel", new Color(70, 70, 70));

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 6, 6, 6);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        searchButton.addActionListener(e -> handleSearch());
        updateButton.addActionListener(e -> handleUpdate());
        cancelButton.addActionListener(e -> dispose());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(220, 220, 220));
        return label;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField(15);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setBackground(new Color(40, 40, 40));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleSearch() {
        String idStr = searchIdField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentStudent = manager.getStudentById(id);
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Student with ID " + id + " not found.", "Search Error", JOptionPane.ERROR_MESSAGE);
            marksLabel.setText("N/A");
            calcGradeLabel.setText("N/A");
            gradeComboBox.setEnabled(false);
            updateButton.setEnabled(false);
        } else {
            marksLabel.setText(String.format("%.2f", currentStudent.getMarks()));
            String autoGrade = Student.calculateGrade(currentStudent.getMarks());
            calcGradeLabel.setText(autoGrade + " (Current: " + currentStudent.getGrade() + ")");
            gradeComboBox.setSelectedItem(currentStudent.getGrade());
            gradeComboBox.setEnabled(true);
            updateButton.setEnabled(true);
        }
    }

    private void handleUpdate() {
        if (currentStudent == null) return;

        String selectedGrade = (String) gradeComboBox.getSelectedItem();
        boolean updated = manager.updateGrade(currentStudent.getStudentId(), selectedGrade);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Grade updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update grade.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
