import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class FeeDetailsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField searchIdField;
    private JButton searchButton;
    private JLabel currentStudentLabel;
    private JTextField totalFeeField;
    private JTextField paidFeeField;
    private JLabel remainingLabel;
    private JLabel statusLabel;
    private JButton updateButton;
    private JTable table;
    private DefaultTableModel model;
    private transient StudentManager manager;
    private MainDashboard dashboard;
    private Student currentStudent;

    public FeeDetailsPanel(MainDashboard dashboard, StudentManager manager) {
        this.dashboard = dashboard;
        this.manager = manager;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(15, 23, 42));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(30, 41, 59));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel headerLabel = new JLabel("Fee Details Management");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        formPanel.add(headerLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        formPanel.add(createLabel("Student ID:"), gbc);

        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(new Color(30, 41, 59));
        searchIdField = createTextField(10);
        searchButton = createButton("Search", new Color(59, 130, 246));
        searchBar.add(searchIdField, BorderLayout.CENTER);
        searchBar.add(searchButton, BorderLayout.EAST);

        gbc.gridx = 1;
        formPanel.add(searchBar, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Student Name:"), gbc);
        currentStudentLabel = new JLabel("N/A");
        currentStudentLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        currentStudentLabel.setForeground(Color.WHITE);
        gbc.gridx = 3;
        formPanel.add(currentStudentLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Total Fee (Rs.):"), gbc);
        totalFeeField = createTextField(10);
        totalFeeField.setEnabled(false);
        gbc.gridx = 1;
        formPanel.add(totalFeeField, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Paid Amount (Rs.):"), gbc);
        paidFeeField = createTextField(10);
        paidFeeField.setEnabled(false);
        gbc.gridx = 3;
        formPanel.add(paidFeeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("Remaining Amount:"), gbc);
        remainingLabel = new JLabel("Rs. 0.00");
        remainingLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        remainingLabel.setForeground(new Color(245, 158, 11));
        gbc.gridx = 1;
        formPanel.add(remainingLabel, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Fee Status:"), gbc);
        statusLabel = new JLabel("N/A");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setForeground(Color.WHITE);
        gbc.gridx = 3;
        formPanel.add(statusLabel, gbc);

        updateButton = createButton("Update Fee Details", new Color(59, 130, 246));
        updateButton.setEnabled(false);

        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(updateButton, gbc);

        searchButton.addActionListener(e -> handleSearch());
        updateButton.addActionListener(e -> handleUpdate());

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(new Color(15, 23, 42));

        String[] columns = {"Student ID", "Student Name", "Department", "Total Fee (Rs.)", "Paid Fee (Rs.)", "Remaining (Rs.)", "Fee Status"};
        model = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setBackground(new Color(30, 41, 59));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(59, 130, 246));
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(51, 65, 85));
        table.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, isSelected, hasFocus, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(30, 41, 59) : new Color(24, 34, 50));
                }
                return c;
            }
        };

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, isSelected, hasFocus, row, col);
                setHorizontalAlignment(JLabel.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(30, 41, 59) : new Color(24, 34, 50));
                }
                return c;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(51, 65, 85));
        header.setForeground(new Color(226, 232, 240));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 41, 59));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));

        tableContainer.add(scrollPane, BorderLayout.CENTER);
        return tableContainer;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(226, 232, 240));
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
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
            currentStudentLabel.setText("N/A");
            totalFeeField.setText("");
            paidFeeField.setText("");
            remainingLabel.setText("Rs. 0.00");
            statusLabel.setText("N/A");
            totalFeeField.setEnabled(false);
            paidFeeField.setEnabled(false);
            updateButton.setEnabled(false);
        } else {
            currentStudentLabel.setText(currentStudent.getStudentName());
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
                statusLabel.setForeground(new Color(16, 185, 129));
            } else {
                statusLabel.setText("Pending");
                statusLabel.setForeground(new Color(239, 68, 68));
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
            updateCalculatedFields();
            JOptionPane.showMessageDialog(this, "Fee details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update fee details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable(ArrayList<Student> students) {
        model.setRowCount(0);
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            Object[] row = {
                s.getStudentId(),
                s.getStudentName(),
                s.getDepartment(),
                String.format("%.2f", s.getTotalFee()),
                String.format("%.2f", s.getPaidFee()),
                String.format("%.2f", s.getRemainingFee()),
                s.getFeeStatus()
            };
            model.addRow(row);
        }
    }
}
