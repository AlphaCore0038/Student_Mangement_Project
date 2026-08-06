import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int studentId;
    private String studentName;
    private double marks;
    private String department;
    private int semester;
    private String section;
    private double attendance;
    private String grade;
    private double totalFee;
    private double paidFee;

    public Student(int studentId, String studentName, double marks) {
        this(studentId, studentName, marks, "Computer Science", 1, "A", 100.0, calculateGrade(marks), 50000.0, 0.0);
    }

    public Student(int studentId, String studentName, double marks, String department, int semester, String section, double attendance, String grade, double totalFee, double paidFee) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.marks = marks;
        this.department = department;
        this.semester = semester;
        this.section = section;
        this.attendance = attendance;
        this.grade = grade != null ? grade : calculateGrade(marks);
        this.totalFee = totalFee;
        this.paidFee = paidFee;
    }

    public static String calculateGrade(double marks) {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }

    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public double getMarks() { return marks; }
    public String getDepartment() { return department; }
    public int getSemester() { return semester; }
    public String getSection() { return section; }
    public double getAttendance() { return attendance; }
    public String getGrade() { return grade; }
    public double getTotalFee() { return totalFee; }
    public double getPaidFee() { return paidFee; }
    public double getRemainingFee() { return Math.max(0, totalFee - paidFee); }
    public String getFeeStatus() { return (totalFee - paidFee <= 0) ? "Paid" : "Pending"; }

    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setMarks(double marks) {
        this.marks = marks;
        this.grade = calculateGrade(marks);
    }
    public void setDepartment(String department) { this.department = department; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setSection(String section) { this.section = section; }
    public void setAttendance(double attendance) { this.attendance = attendance; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setTotalFee(double totalFee) { this.totalFee = totalFee; }
    public void setPaidFee(double paidFee) { this.paidFee = paidFee; }

    public void display() {
        System.out.println("ID: " + studentId + " | Name: " + studentName + " | Marks: " + marks + " | Dept: " + department + " | Sem: " + semester + " | Sec: " + section + " | Att: " + attendance + "% | Grade: " + grade + " | Fee Status: " + getFeeStatus());
    }
}
