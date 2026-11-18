package service;

import model.Grade;
import model.Student;

// Категория успеваемости студента
public enum Category {
    EXCELLENT("Отличники"),
    GOOD("Хорошисты"),
    SATISFACTORY("Троечники"),
    DEBTOR("Задолжники");

    private final String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static Category of(Student student) {
        if (!student.getDebts().isEmpty()) return DEBTOR;
        int min = student.getGrades().stream().mapToInt(Grade::getMark).min().orElse(0);
        if (min == 5) return EXCELLENT;
        if (min >= 4) return GOOD;
        return SATISFACTORY;
    }
}
