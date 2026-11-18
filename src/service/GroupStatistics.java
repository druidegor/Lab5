package service;

import exception.EmptyGroupException;
import model.Grade;
import model.Group;
import model.Student;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupStatistics implements StudentAnalytics, SubjectAnalytics {
    private static final Logger logger = Logger.getLogger(GroupStatistics.class.getName());

    private void checkGroupNotEmpty(Group group) throws EmptyGroupException {
        if (group == null || group.getStudents().isEmpty()) {
            logger.warning("Попытка рассчитать статистику для пустой группы.");
            throw new EmptyGroupException("В группе нет ни одного студента для расчёта статистики.");
        }
    }

    public Map<String, SubjectStats> bySubject(Group group) throws EmptyGroupException {
        checkGroupNotEmpty(group);
        logger.fine("Расчёт статистики по предметам...");

        Map<String, List<Integer>> marks = new LinkedHashMap<>();
        for (Student s : group.getStudents()) {
            for (Grade g : s.getGrades()) {
                marks.computeIfAbsent(g.getSubject(), k -> new ArrayList<>()).add(g.getMark());
            }
        }
        Map<String, SubjectStats> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<Integer>> e : marks.entrySet()) {
            IntSummaryStatistics st = e.getValue().stream()
                    .mapToInt(Integer::intValue)
                    .summaryStatistics();
            result.put(e.getKey(), new SubjectStats(e.getKey(), st.getAverage(),
                    st.getMin(), st.getMax(), (int) st.getCount()));
        }
        logger.log(Level.INFO, "Рассчитана статистика по {0} предметам.", result.size());
        return result;
    }

    public Map<Category, List<Student>> byCategory(Group group) throws EmptyGroupException {
        checkGroupNotEmpty(group);
        logger.fine("Распределение студентов по категориям успеваемости...");

        Map<Category, List<Student>> result = new EnumMap<>(Category.class);
        for (Category c : Category.values()) {
            result.put(c, new ArrayList<>());
        }
        for (Student s : group.getStudents()) {
            result.get(Category.of(s)).add(s);
        }
        return result;
    }

    public Map<Integer, Integer> markDistribution(Group group) throws EmptyGroupException {
        checkGroupNotEmpty(group);
        logger.fine("Подсчёт распределения оценок...");

        Map<Integer, Integer> result = new TreeMap<>(Comparator.reverseOrder());
        for (Student s : group.getStudents()) {
            for (Grade g : s.getGrades()) {
                result.merge(g.getMark(), 1, Integer::sum);
            }
        }
        return result;
    }
}