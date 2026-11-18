package model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final String name;
    private final int course;
    private final List<Student> students = new ArrayList<>();

    public Group(String name, int course) {
        this.name = name;
        this.course = course;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public double getAverage() {
        int sum = 0, count = 0;
        for (Student s : students) {
            for (Grade g : s.getGrades()) {
                sum += g.getMark();
                count++;
            }
        }
        return count == 0 ? 0 : (double) sum / count;
    }

    public Student getBestStudent() {
        Student best = null;
        for (Student s : students) {
            if (best == null || s.getAverage() > best.getAverage()) best = s;
        }
        return best;
    }

    public List<Student> getDebtors() {
        List<Student> debtors = new ArrayList<>();
        for (Student s : students) {
            if (!s.getDebts().isEmpty()) debtors.add(s);
        }
        return debtors;
    }

    public String getName()            { return name; }
    public int getCourse()             { return course; }
    public List<Student> getStudents() { return students; }
}
