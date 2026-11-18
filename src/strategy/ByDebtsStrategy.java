package strategy;

import model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ByDebtsStrategy implements SortStrategy {

    @Override
    public String getName() {
        return "по задолженностям";
    }

    @Override
    public List<Student> sort(List<Student> students) {
        List<Student> result = new ArrayList<>(students);
        result.sort(Comparator.comparingInt((Student s) -> s.getDebts().size()).reversed()
                .thenComparingDouble(Student::getAverage));
        return result;
    }
}
