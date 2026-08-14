import java.io.*;
import java.util.ArrayList;

public class StudentManager {
    private ArrayList<Student> students = new ArrayList<Student>();
    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = "data/students.dat";

    public StudentManager() {
        loadData();
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        File folder = new File(DATA_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    students = (ArrayList<Student>) obj;
                }
            } catch (Exception e) {
                students = new ArrayList<Student>();
            }
        } else {
            students = new ArrayList<Student>();
            saveData();
        }
    }

    public void saveData() {
        File folder = new File(DATA_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(students);
        } catch (Exception ignored) {
        }
    }

    public boolean addStudent(Student s) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId() == s.getStudentId()) {
                return false;
            }
        }
        students.add(s);
        saveData();
        return true;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Student getStudentById(int id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId() == id) {
                return students.get(i);
            }
        }
        return null;
    }

    public void searchById(int id) {
        Student s = getStudentById(id);
        if (s != null) {
            System.out.println("Student found:");
            s.display();
        } else {
            System.out.println("Student not found.");
        }
    }

    public boolean deleteStudent(int id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId() == id) {
                students.remove(i);
                saveData();
                return true;
            }
        }
        return false;
    }

    public boolean updateMarks(int id, double newMarks) {
        Student s = getStudentById(id);
        if (s != null) {
            s.setMarks(newMarks);
            saveData();
            return true;
        }
        return false;
    }

    public boolean updateStudentProfile(int id, String name, String dept, int sem, String section) {
        Student s = getStudentById(id);
        if (s != null) {
            s.setStudentName(name);
            s.setDepartment(dept);
            s.setSemester(sem);
            s.setSection(section);
            saveData();
            return true;
        }
        return false;
    }

    public boolean updateAttendance(int id, double attendance) {
        Student s = getStudentById(id);
        if (s != null) {
            s.setAttendance(attendance);
            saveData();
            return true;
        }
        return false;
    }

    public boolean updateGrade(int id, String grade) {
        Student s = getStudentById(id);
        if (s != null) {
            s.setGrade(grade);
            saveData();
            return true;
        }
        return false;
    }

    public boolean updateFeeDetails(int id, double totalFee, double paidFee) {
        Student s = getStudentById(id);
        if (s != null) {
            s.setTotalFee(totalFee);
            s.setPaidFee(paidFee);
            saveData();
            return true;
        }
        return false;
    }

    public double getAverageMarks() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double total = 0;
        for (int i = 0; i < students.size(); i++) {
            total += students.get(i).getMarks();
        }
        return total / students.size();
    }

    public Student getTopper() {
        if (students.isEmpty()) {
            return null;
        }
        Student top = students.get(0);
        for (int i = 1; i < students.size(); i++) {
            if (students.get(i).getMarks() > top.getMarks()) {
                top = students.get(i);
            }
        }
        return top;
    }

    public int getTotalStudents() {
        return students.size();
    }

    public void viewAll() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n--- Student List ---");
        for (int i = 0; i < students.size(); i++) {
            students.get(i).display();
        }
    }

    public void showAverage() {
        if (students.isEmpty()) {
            System.out.println("No students to calculate average.");
            return;
        }
        System.out.println("Average Marks: " + getAverageMarks());
    }

    public void showTopper() {
        Student top = getTopper();
        if (top == null) {
            System.out.println("No students available.");
            return;
        }
        System.out.println("Topper:");
        top.display();
    }
}
//...
