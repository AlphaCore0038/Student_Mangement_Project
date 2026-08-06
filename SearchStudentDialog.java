import javax.swing.*;
import java.awt.*;

public class SearchStudentDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField idField;
    private JButton searchButton;
    private JButton cancelButton;
    private transient StudentManager manager;

    public SearchStudentDialog(MainDashboard parent, StudentManager manager) {
        super(parent, "Search Student", true);
        this.manager = manager;

        setSize(400, 240);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 41, 59));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Search Student", SwingConstants.CENTER);
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
        idLabel.setForeground(new Color(226, 232, 240));
        mainPanel.add(idLabel, gbc);

        idField = new JTextField(15);
        idField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        idField.setBackground(new Color(15, 23, 42));
        idField.setForeground(Color.WHITE);
        idField.setCaretColor(Color.WHITE);
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridx = 1;
        mainPanel.add(idField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(30, 41, 59));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchButton.setBackground(new Color(59, 130, 246));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelButton.setBackground(new Color(51, 65, 85));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        searchButton.addActionListener(e -> handleSearch());
        cancelButton.addActionListener(e -> dispose());
    }

    private void handleSearch() {
        String idStr = idField.getText().trim();
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

        Student student = manager.getStudentById(id);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student with ID " + id + " not found.", "Search Result", JOptionPane.ERROR_MESSAGE);
        } else {
            String details = String.format("Student Details:\n\nID: %d\nName: %s\nDepartment: %s\nSemester: %d\nSection: %s\nMarks: %.2f\nGrade: %s\nAttendance: %.1f%%\nTotal Fee: Rs. %,.2f\nPaid Fee: Rs. %,.2f\nFee Status: %s",
                    student.getStudentId(), student.getStudentName(), student.getDepartment(), student.getSemester(), student.getSection(),
                    student.getMarks(), student.getGrade(), student.getAttendance(), student.getTotalFee(), student.getPaidFee(), student.getFeeStatus());
            JOptionPane.showMessageDialog(this, details, "Student Information", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
