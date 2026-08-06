import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class StudentTablePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;

    public StudentTablePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42));

        String[] columns = {"Student ID", "Student Name", "Department", "Semester", "Section", "Marks", "Grade", "Attendance", "Fee Status"};
        model = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
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
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
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
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(51, 65, 85));
        header.setForeground(new Color(226, 232, 240));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 41, 59));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable(ArrayList<Student> students) {
        model.setRowCount(0);
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            Object[] row = {
                s.getStudentId(),
                s.getStudentName(),
                s.getDepartment(),
                s.getSemester(),
                s.getSection(),
                String.format("%.2f", s.getMarks()),
                s.getGrade(),
                String.format("%.1f%%", s.getAttendance()),
                s.getFeeStatus()
            };
            model.addRow(row);
        }
    }
}
