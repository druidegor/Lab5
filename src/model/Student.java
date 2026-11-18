package model;

import annotation.AcademicMetric;
import config.AppConfig;
import exception.InvalidGradeException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Student {
    public static final Logger logger = Logger.getLogger(Student.class.getName());

    private final int id;
    private final String lastName;
    private final String firstName;
    private final LocalDate birthDate;
    private final List<Grade> grades;

    private Student(Builder b) {
        this.id = b.id;
        this.lastName = b.lastName;
        this.firstName = b.firstName;
        this.birthDate = b.birthDate;
        this.grades = new ArrayList<>(b.grades);
    }

    @AcademicMetric(description = "Расчёт среднего балла студента", priority = 1)
    public double getAverage() {
        if (grades.isEmpty()) return 0;
        int sum = 0;
        for (Grade g : grades) sum += g.getMark();
        return (double) sum / grades.size();
    }

    @AcademicMetric(description = "Выявление предметов с задолженностями", priority = 2)
    public List<String> getDebts() {
        List<String> debts = new ArrayList<>();
        for (Grade g : grades) {
            if (g.getMark() == AppConfig.getInstance().getDebtMark()) debts.add(g.getSubject());
        }
        return debts;
    }

    public String getFullName() { return lastName + " " + firstName; }
    public int getId()               { return id; }
    public LocalDate getBirthDate()  { return birthDate; }
    public List<Grade> getGrades()   { return Collections.unmodifiableList(grades); }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private int id;
        private String lastName;
        private String firstName;
        private LocalDate birthDate;
        private final List<Grade> grades = new ArrayList<>();

        public Builder id(int id) { this.id = id; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder birthDate(LocalDate date) { this.birthDate = date; return this; }

        public Builder grade(String subject, int mark) throws InvalidGradeException {
            grades.add(new Grade(subject, mark));
            return this;
        }

        public Student build() {
            if (id <= 0)
                throw new IllegalStateException("id студента должен быть положительным");
            if (lastName == null || lastName.isBlank())
                throw new IllegalStateException("Не указана фамилия студента " + id);
            if (firstName == null || firstName.isBlank())
                throw new IllegalStateException("Не указано имя студента " + id);
            if (birthDate == null)
                throw new IllegalStateException("Не указана дата рождения студента " + id);
            Student student = new Student(this);
            logger.fine(() -> "Собран объект студента: " + student.getFullName() + " (ID: " + id + ")");
            return student;
        }
    }
}