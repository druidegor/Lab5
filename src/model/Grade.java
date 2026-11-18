package model;

import exception.InvalidGradeException;

public class Grade {

    private final String subject;
    private final int mark;

    public Grade(String subject, int mark) throws InvalidGradeException {
        if (subject == null || subject.isBlank()) {
            throw new InvalidGradeException("Название предмета не может быть пустым.");
        }
        if (mark < 2 || mark > 5) {
            throw new InvalidGradeException("Недопустимая оценка " + mark + " по предмету \"" + subject + "\". Допустимы значения от 2 до 5.");
        }
        this.subject = subject.trim();
        this.mark = mark;
    }

    public String getSubject() { return subject; }
    public int getMark() { return mark; }

    @Override
    public String toString() {
        return subject + ": " + mark;
    }

}
