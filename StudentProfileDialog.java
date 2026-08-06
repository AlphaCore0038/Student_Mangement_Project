import javax.swing.*;
import java.awt.*;

public class StudentProfileDialog extends JDialog {
    private JTextField searchIdField;
    private JButton searchButton;
    private JTextField nameField;
    private JTextField deptField;
    private JTextField semField;
    private JTextField secField;
    private JButton updateButton;
    private JButton cancelButton;
    private StudentManager manager;
    private MainDashboard dashboard;
    private Student currentStudent;

    public StudentProfileDialog(MainDashboard parent, StudentManager manager) {
        super(parent, "Student Profile Management", true);
        this.dashboard = parent;
        this.manager = manager;

        setSize(460, 480);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(28, 28, 28));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Student Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(createLabel("Search Student ID:"), gbc);

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
        mainPanel.add(createLabel("Student Name:"), gbc);
        nameField = createTextField();
        nameField.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(createLabel("Department:"), gbc);
        deptField = createTextField();
        deptField.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(deptField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(createLabel("Semester:"), gbc);
        semField = createTextField();
        semField.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(semField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(createLabel("Section:"), gbc);
        secField = createTextField();
        secField.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(secField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(28, 28, 28));

        updateButton = createButton("Update Profile", new Color(0, 102, 204));
        updateButton.setEnabled(false);
        cancelButton = createButton("Cancel", new Color(70, 70, 70));

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
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
        tf.setDisabledTextColor(new Color(170, 170, 170));
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
            setFieldsEnabled(false);
        } else {
            nameField.setText(currentStudent.getStudentName());
            deptField.setText(currentStudent.getDepartment());
            semField.setText(String.valueOf(currentStudent.getSemester()));
            secField.setText(currentStudent.getSection());
            setFieldsEnabled(true);
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        deptField.setEnabled(enabled);
        semField.setEnabled(enabled);
        secField.setEnabled(enabled);
        updateButton.setEnabled(enabled);
    }

    private void handleUpdate() {
        if (currentStudent == null) return;

        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        String semStr = semField.getText().trim();
        String sec = secField.getText().trim();

        if (name.isEmpty() || dept.isEmpty() || semStr.isEmpty() || sec.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int sem;
        try {
            sem = Integer.parseInt(semStr);
            if (sem <= 0 || sem > 12) {
                JOptionPane.showMessageDialog(this, "Semester must be a valid number between 1 and 12.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Semester must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = manager.updateStudentProfile(currentStudent.getStudentId(), name, dept, sem, sec);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Student Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
