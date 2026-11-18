package strategy;

import model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ByAverageStrategy implements SortStrategy {

    @Override
    public String getName() {
        return "по среднему баллу";
    }

    @Override
    public List<Student> sort(List<Student> students) {
        List<Student> result = new ArrayList<>(students);
        result.sort(Comparator.comparingDouble(Student::getAverage).reversed());
        return result;
    }
}
