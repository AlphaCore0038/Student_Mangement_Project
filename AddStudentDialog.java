import javax.swing.*;
import java.awt.*;

public class AddStudentDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField idField;
    private JTextField nameField;
    private JTextField deptField;
    private JTextField semField;
    private JTextField secField;
    private JTextField marksField;
    private JButton saveButton;
    private JButton cancelButton;
    private transient StudentManager manager;
    private MainDashboard dashboard;

    public AddStudentDialog(MainDashboard parent, StudentManager manager) {
        super(parent, "Add New Student", true);
        this.dashboard = parent;
        this.manager = manager;

        setSize(440, 440);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 41, 59));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add New Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(createLabel("Student ID:"), gbc);
        idField = createTextField();
        gbc.gridx = 1;
        mainPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(createLabel("Student Name:"), gbc);
        nameField = createTextField();
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(createLabel("Department:"), gbc);
        deptField = createTextField();
        deptField.setText("Computer Science");
        gbc.gridx = 1;
        mainPanel.add(deptField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(createLabel("Semester:"), gbc);
        semField = createTextField();
        semField.setText("1");
        gbc.gridx = 1;
        mainPanel.add(semField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(createLabel("Section:"), gbc);
        secField = createTextField();
        secField.setText("A");
        gbc.gridx = 1;
        mainPanel.add(secField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(createLabel("Marks (0-100):"), gbc);
        marksField = createTextField();
        gbc.gridx = 1;
        mainPanel.add(marksField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(30, 41, 59));

        saveButton = createButton("Save Student", new Color(59, 130, 246));
        cancelButton = createButton("Cancel", new Color(51, 65, 85));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 6, 6, 6);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(new Color(226, 232, 240));
        return label;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField(15);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setBackground(new Color(15, 23, 42));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
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
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleSave() {
        String idStr = idField.getText().trim();
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        String semStr = semField.getText().trim();
        String sec = secField.getText().trim();
        String marksStr = marksField.getText().trim();

        if (idStr.isEmpty() || name.isEmpty() || dept.isEmpty() || semStr.isEmpty() || sec.isEmpty() || marksStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "Student ID must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Student ID must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int sem;
        try {
            sem = Integer.parseInt(semStr);
            if (sem <= 0 || sem > 12) {
                JOptionPane.showMessageDialog(this, "Semester must be between 1 and 12.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Semester must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double marks;
        try {
            marks = Double.parseDouble(marksStr);
            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Marks must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(id, name, marks, dept, sem, sec, 100.0, Student.calculateGrade(marks), 50000.0, 0.0);
        boolean added = manager.addStudent(student);

        if (!added) {
            JOptionPane.showMessageDialog(this, "Student with ID " + id + " already exists!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dashboard.refreshData();
        dispose();
    }
}
