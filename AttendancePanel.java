import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class AttendancePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField searchIdField;
    private JButton searchButton;
    private JLabel currentStudentLabel;
    private JLabel currentAttLabel;
    private JTextField newAttField;
    private JButton updateButton;
    private JTable table;
    private DefaultTableModel model;
    private transient StudentManager manager;
    private MainDashboard dashboard;
    private Student currentStudent;

    public AttendancePanel(MainDashboard dashboard, StudentManager manager) {
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

        JLabel headerLabel = new JLabel("Attendance Management");
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
        formPanel.add(createLabel("Current Attendance:"), gbc);
        currentAttLabel = new JLabel("N/A");
        currentAttLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        currentAttLabel.setForeground(new Color(16, 185, 129));
        gbc.gridx = 1;
        formPanel.add(currentAttLabel, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("New Attendance (%):"), gbc);
        newAttField = createTextField(10);
        newAttField.setEnabled(false);
        gbc.gridx = 3;
        formPanel.add(newAttField, gbc);

        updateButton = createButton("Update Attendance", new Color(59, 130, 246));
        updateButton.setEnabled(false);

        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(updateButton, gbc);

        searchButton.addActionListener(e -> handleSearch());
        updateButton.addActionListener(e -> handleUpdate());

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(new Color(15, 23, 42));

        String[] columns = {"Student ID", "Student Name", "Department", "Attendance (%)", "Status"};
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
            currentAttLabel.setText("N/A");
            newAttField.setEnabled(false);
            updateButton.setEnabled(false);
        } else {
            currentStudentLabel.setText(currentStudent.getStudentName());
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
            currentAttLabel.setText(String.format("%.1f%%", currentStudent.getAttendance()));
            JOptionPane.showMessageDialog(this, "Attendance updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dashboard.refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update attendance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable(ArrayList<Student> students) {
        model.setRowCount(0);
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            String status = s.getAttendance() >= 75.0 ? "Good" : "Low";
            Object[] row = {
                s.getStudentId(),
                s.getStudentName(),
                s.getDepartment(),
                String.format("%.1f%%", s.getAttendance()),
                status
            };
            model.addRow(row);
        }
    }
}
