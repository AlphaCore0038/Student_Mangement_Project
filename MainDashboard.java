import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainDashboard extends JFrame {
    private static final long serialVersionUID = 1L;

    private transient StudentManager manager;
    private StudentTablePanel tablePanel;
    private StatisticsPanel statisticsPanel;
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private Map<String, JButton> navButtons = new HashMap<>();

    private StudentProfilePanel profilePanel;
    private MarksPanel marksPanel;
    private AttendancePanel attendancePanel;
    private GradesPanel gradesPanel;
    private FeeDetailsPanel feePanel;

    public MainDashboard(StudentManager manager) {
        this.manager = manager;

        setTitle("Student Management System");
        setSize(1180, 740);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));
        rootPanel.setBackground(new Color(15, 23, 42));
        setContentPane(rootPanel);

        rootPanel.add(createSidebar(), BorderLayout.WEST);

        JPanel rightContainer = new JPanel(new BorderLayout(0, 0));
        rightContainer.setBackground(new Color(15, 23, 42));

        rightContainer.add(createTopHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(15, 23, 42));

        JPanel overviewView = new JPanel(new BorderLayout(0, 15));
        overviewView.setBackground(new Color(15, 23, 42));
        overviewView.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statisticsPanel = new StatisticsPanel();
        overviewView.add(statisticsPanel, BorderLayout.NORTH);

        tablePanel = new StudentTablePanel();
        overviewView.add(tablePanel, BorderLayout.CENTER);

        overviewView.add(createQuickActionsBar(), BorderLayout.SOUTH);

        profilePanel = new StudentProfilePanel(this, manager);
        marksPanel = new MarksPanel(this, manager);
        attendancePanel = new AttendancePanel(this, manager);
        gradesPanel = new GradesPanel(this, manager);
        feePanel = new FeeDetailsPanel(this, manager);

        mainContentPanel.add(overviewView, "OVERVIEW");
        mainContentPanel.add(profilePanel, "PROFILE");
        mainContentPanel.add(marksPanel, "MARKS");
        mainContentPanel.add(attendancePanel, "ATTENDANCE");
        mainContentPanel.add(gradesPanel, "GRADES");
        mainContentPanel.add(feePanel, "FEE");

        rightContainer.add(mainContentPanel, BorderLayout.CENTER);
        rootPanel.add(rightContainer, BorderLayout.CENTER);

        switchView("OVERVIEW");
        refreshData();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        Font primaryMonoBold = new Font("JetBrains Mono", Font.BOLD, 22);
        if (!primaryMonoBold.getFamily().equalsIgnoreCase("JetBrains Mono")) {
            primaryMonoBold = new Font("Consolas", Font.BOLD, 22);
        }

        Font primaryMonoPlain = new Font("JetBrains Mono", Font.PLAIN, 13);
        if (!primaryMonoPlain.getFamily().equalsIgnoreCase("JetBrains Mono")) {
            primaryMonoPlain = new Font("Consolas", Font.PLAIN, 13);
        }

        JLabel brandLabel = new JLabel("students_hub");
        brandLabel.setFont(primaryMonoBold);
        brandLabel.setForeground(new Color(56, 189, 248));
        brandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(brandLabel);

        JLabel subLabel = new JLabel("Academic Portal");
        subLabel.setFont(primaryMonoPlain);
        subLabel.setForeground(new Color(148, 163, 184));
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subLabel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 25)));

        navButtons.put("OVERVIEW", createNavButton("Dashboard Overview", "OVERVIEW"));
        navButtons.put("PROFILE", createNavButton("Student Profile", "PROFILE"));
        navButtons.put("MARKS", createNavButton("Marks", "MARKS"));
        navButtons.put("ATTENDANCE", createNavButton("Attendance", "ATTENDANCE"));
        navButtons.put("GRADES", createNavButton("Grades", "GRADES"));
        navButtons.put("FEE", createNavButton("Fee Details", "FEE"));

        for (JButton btn : navButtons.values()) {
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton exitBtn = new JButton("Logout / Exit");
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        exitBtn.setBackground(new Color(225, 29, 72));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setMaximumSize(new Dimension(200, 40));
        exitBtn.setPreferredSize(new Dimension(200, 40));
        exitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to exit?", "Exit Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        sidebar.add(exitBtn);

        return sidebar;
    }

    private JButton createNavButton(String text, String viewKey) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        btn.addActionListener(e -> switchView(viewKey));
        return btn;
    }

    private void switchView(String viewKey) {
        cardLayout.show(mainContentPanel, viewKey);

        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(viewKey)) {
                entry.getValue().setBackground(new Color(59, 130, 246));
                entry.getValue().setForeground(Color.WHITE);
            } else {
                entry.getValue().setBackground(new Color(30, 41, 59));
                entry.getValue().setForeground(new Color(203, 213, 225));
            }
        }
    }

    private JPanel createTopHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 23, 42));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(51, 65, 85)),
            BorderFactory.createEmptyBorder(18, 24, 18, 24)
        ));

        JLabel titleLabel = new JLabel("Student Management Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Academic Performance & Records Management System");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(148, 163, 184));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createQuickActionsBar() {
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionBar.setBackground(new Color(15, 23, 42));

        JButton addBtn = createBarButton("Add New Student", new Color(59, 130, 246));
        JButton searchBtn = createBarButton("Search Student", new Color(59, 130, 246));
        JButton deleteBtn = createBarButton("Delete Student", new Color(225, 29, 72));
        JButton refreshBtn = createBarButton("Refresh Data", new Color(51, 65, 85));

        addBtn.addActionListener(e -> new AddStudentDialog(this, manager).setVisible(true));
        searchBtn.addActionListener(e -> new SearchStudentDialog(this, manager).setVisible(true));
        deleteBtn.addActionListener(e -> new DeleteStudentDialog(this, manager).setVisible(true));
        refreshBtn.addActionListener(e -> refreshData());

        actionBar.add(addBtn);
        actionBar.add(searchBtn);
        actionBar.add(deleteBtn);
        actionBar.add(refreshBtn);

        return actionBar;
    }

    private JButton createBarButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return btn;
    }

    public void refreshData() {
        tablePanel.refreshTable(manager.getStudents());
        statisticsPanel.updateStatistics(manager);
        if (profilePanel != null) profilePanel.refreshTable(manager.getStudents());
        if (marksPanel != null) marksPanel.refreshTable(manager.getStudents());
        if (attendancePanel != null) attendancePanel.refreshTable(manager.getStudents());
        if (gradesPanel != null) gradesPanel.refreshTable(manager.getStudents());
        if (feePanel != null) feePanel.refreshTable(manager.getStudents());
    }
}
//...
