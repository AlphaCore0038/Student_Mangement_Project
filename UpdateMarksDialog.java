import javax.swing.*;
import java.awt.*;

public class UpdateMarksDialog extends JDialog {
    private JTextField idField;
    private JTextField marksField;
    private JButton updateButton;
    private JButton cancelButton;
    private StudentManager manager;
    private MainDashboard dashboard;

    public UpdateMarksDialog(MainDashboard parent, StudentManager manager) {
        super(parent, "Update Student Marks", true);
        this.dashboard = parent;
        this.manager = manager;

        setSize(420, 300);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(28, 28, 28));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Update Marks", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        idLabel.setForeground(new Color(220, 220, 220));
        mainPanel.add(idLabel, gbc);

        idField = createTextField();
        gbc.gridx = 1;
        mainPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel marksLabel = new JLabel("New Marks (0-100):");
        marksLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        marksLabel.setForeground(new Color(220, 220, 220));
        mainPanel.add(marksLabel, gbc);

        marksField = createTextField();
        gbc.gridx = 1;
        mainPanel.add(marksField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(28, 28, 28));

        updateButton = createButton("Update", new Color(0, 102, 204));
        cancelButton = createButton("Cancel", new Color(70, 70, 70));

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        updateButton.addActionListener(e -> handleUpdate());
        cancelButton.addActionListener(e -> dispose());
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
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleUpdate() {
        String idStr = idField.getText().trim();
        String marksStr = marksField.getText().trim();

        if (idStr.isEmpty() || marksStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both Student ID and New Marks are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double newMarks;
        try {
            newMarks = Double.parseDouble(marksStr);
            if (newMarks < 0 || newMarks > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Marks must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = manager.updateMarks(id, newMarks);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Marks updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Student with ID " + id + " not found.", "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
//...
