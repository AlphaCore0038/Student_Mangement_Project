import javax.swing.*;
import java.awt.*;

public class AttendanceDialog extends JDialog {
    private JTextField searchIdField;
    private JButton searchButton;
    private JLabel currentAttLabel;
    private JTextField newAttField;
    private JButton updateButton;
    private JButton cancelButton;
    private StudentManager manager;
    private MainDashboard dashboard;
    private Student currentStudent;

    public AttendanceDialog(MainDashboard parent, StudentManager manager) {
        super(parent, "Attendance Management", true);
        this.dashboard = parent;
        this.manager = manager;

        setSize(420, 320);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(28, 28, 28));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Attendance Management", SwingConstants.CENTER);
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
        mainPanel.add(createLabel("Current Attendance:"), gbc);
        currentAttLabel = new JLabel("N/A");
        currentAttLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        currentAttLabel.setForeground(new Color(0, 200, 140));
        gbc.gridx = 1;
        mainPanel.add(currentAttLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(createLabel("New Attendance (%):"), gbc);
        newAttField = createTextField();
        newAttField.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(newAttField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(28, 28, 28));

        updateButton = createButton("Update Attendance", new Color(0, 102, 204));
        updateButton.setEnabled(false);
        cancelButton = createButton("Cancel", new Color(70, 70, 70));

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
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
            currentAttLabel.setText("N/A");
            newAttField.setEnabled(false);
            updateButton.setEnabled(false);
        } else {
            currentAttLabel.setText(String.format("%.1f%%", currentStudent.getAttendance()));
            newAttField.setText("");
            newAttField.setEnabled(true);
            updateButton.setEnabled(true);
        }
    }

    private void handleUpdate() {
        if (currentStudent == null) return;

        String attStr = newAttField.getText().trim();
        if (attStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter new attendance percentage.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double att;
        try {
            att = Double.parseDouble(attStr);
            if (att < 0 || att > 100) {
                JOptionPane.showMessageDialog(this, "Attendance percentage must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Attendance must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = manager.updateAttendance(currentStudent.getStudentId(), att);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Attendance updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update attendance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
//...
