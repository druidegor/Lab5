package strategy;

import exception.EmptyGroupException;
import model.Group;
import model.Student;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentRanking {
    private static final Logger logger = Logger.getLogger(StudentRanking.class.getName());
    private SortStrategy strategy;

    public StudentRanking(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy strategy) {
        this.strategy = strategy;
        logger.log(Level.CONFIG, "Установлена стратегия сортировки: {0}", strategy.getName());
    }

    public String getStrategyName() {
        return strategy.getName();
    }

    public List<Student> rank(Group group) throws EmptyGroupException {
        if (group == null || group.getStudents().isEmpty()) {
            logger.warning("Попытка построить рейтинг для пустой группы.");
            throw new EmptyGroupException("Невозможно построить рейтинг: список студентов пуст.");
        }
        logger.log(Level.INFO, "Формирование рейтинга студентов ({0})", strategy.getName());
        return strategy.sort(group.getStudents());
    }
}