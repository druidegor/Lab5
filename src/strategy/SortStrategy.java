package strategy;

import model.Student;

import java.util.List;

public interface SortStrategy {

    String getName();

    List<Student> sort(List<Student> students);
}
