package strategy;

import model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ByLastNameStrategy implements SortStrategy {

    @Override
    public String getName() {
        return "по алфавиту";
    }

    @Override
    public List<Student> sort(List<Student> students) {
        List<Student> result = new ArrayList<>(students);
        result.sort(Comparator.comparing(Student::getFullName));
        return result;
    }
}
