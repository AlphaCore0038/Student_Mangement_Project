import javax.swing.*;
import java.awt.*;

public class FeeDetailsDialog extends JDialog {
    private JTextField searchIdField;
    private JButton searchButton;
    private JTextField totalFeeField;
    private JTextField paidFeeField;
    private JLabel remainingLabel;
    private JLabel statusLabel;
    private JButton updateButton;
    private JButton cancelButton;
    private StudentManager manager;
    private MainDashboard dashboard;
    private Student currentStudent;

    public FeeDetailsDialog(MainDashboard parent, StudentManager manager) {
        super(parent, "Fee Details Management", true);
        this.dashboard = parent;
        this.manager = manager;

        setSize(460, 420);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(28, 28, 28));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Fee Details Management", SwingConstants.CENTER);
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
        mainPanel.add(createLabel("Total Fee (Rs.):"), gbc);
        totalFeeField = createTextField();
        totalFeeField.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(totalFeeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(createLabel("Paid Amount (Rs.):"), gbc);
        paidFeeField = createTextField();
        paidFeeField.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(paidFeeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(createLabel("Remaining Amount:"), gbc);
        remainingLabel = new JLabel("Rs. 0.00");
        remainingLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        remainingLabel.setForeground(new Color(220, 180, 40));
        gbc.gridx = 1;
        mainPanel.add(remainingLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(createLabel("Fee Status:"), gbc);
        statusLabel = new JLabel("N/A");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(statusLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(28, 28, 28));

        updateButton = createButton("Update Fee", new Color(0, 102, 204));
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
            totalFeeField.setText("");
            paidFeeField.setText("");
            remainingLabel.setText("Rs. 0.00");
            statusLabel.setText("N/A");
            totalFeeField.setEnabled(false);
            paidFeeField.setEnabled(false);
            updateButton.setEnabled(false);
        } else {
            totalFeeField.setText(String.format("%.2f", currentStudent.getTotalFee()));
            paidFeeField.setText(String.format("%.2f", currentStudent.getPaidFee()));
            updateCalculatedFields();
            totalFeeField.setEnabled(true);
            paidFeeField.setEnabled(true);
            updateButton.setEnabled(true);
        }
    }

    private void updateCalculatedFields() {
        if (currentStudent == null) return;
        try {
            double total = Double.parseDouble(totalFeeField.getText().trim());
            double paid = Double.parseDouble(paidFeeField.getText().trim());
            double remaining = Math.max(0, total - paid);
            remainingLabel.setText(String.format("Rs. %,.2f", remaining));
            if (remaining <= 0) {
                statusLabel.setText("Paid");
                statusLabel.setForeground(new Color(40, 180, 120));
            } else {
                statusLabel.setText("Pending");
                statusLabel.setForeground(new Color(220, 80, 80));
            }
        } catch (Exception ignored) {
        }
    }

    private void handleUpdate() {
        if (currentStudent == null) return;

        String totalStr = totalFeeField.getText().trim();
        String paidStr = paidFeeField.getText().trim();

        if (totalStr.isEmpty() || paidStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total Fee and Paid Amount are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalFee, paidFee;
        try {
            totalFee = Double.parseDouble(totalStr);
            paidFee = Double.parseDouble(paidStr);
            if (totalFee < 0 || paidFee < 0) {
                JOptionPane.showMessageDialog(this, "Amounts cannot be negative.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (paidFee > totalFee) {
                JOptionPane.showMessageDialog(this, "Paid Amount cannot exceed Total Fee.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fee values must be valid numbers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = manager.updateFeeDetails(currentStudent.getStudentId(), totalFee, paidFee);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Fee details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update fee details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
