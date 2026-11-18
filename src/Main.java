import annotation.ReflectionAnalyzer;
import config.AppConfig;
import config.LoggerConfig;
import exception.EmptyGroupException;
import exception.InvalidGradeException;
import model.Grade;
import model.Group;
import model.Student;
import org.xml.sax.SAXException;
import parser.DomGroupParser;
import parser.GroupParser;
import parser.XmlValidator;
import service.Category;
import service.GroupStatistics;
import service.StudentAnalytics;
import service.SubjectAnalytics;
import service.SubjectStats;
import strategy.ByAverageStrategy;
import strategy.ByDebtsStrategy;
import strategy.ByLastNameStrategy;
import strategy.SortStrategy;
import strategy.StudentRanking;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            LoggerConfig.setup();

            logger.info("Старт приложения обработки успеваемости группы.");

            AppConfig config = AppConfig.getInstance();
            File xmlFile = new File(config.getXmlPath());
            File xsdFile = new File(config.getXsdPath());

            XmlValidator.validate(xmlFile, xsdFile);
            System.out.println("group.xml соответствует схеме group.xsd");

            GroupParser parser = new DomGroupParser();
            Group group = parser.parse(xmlFile);

            GroupStatistics statisticsImpl = new GroupStatistics();
            SubjectAnalytics subjectAnalytics = statisticsImpl;
            StudentAnalytics studentAnalytics = statisticsImpl;

            printReport(group);
            printStatistics(group, subjectAnalytics, studentAnalytics);
            printRankings(group);

            ReflectionAnalyzer.analyzeStudentMetrics(group.getBestStudent());

        } catch (SAXException | InvalidGradeException e) {
            System.out.println("Ошибка валидации данных или XML: " + e.getMessage());
            logger.log(Level.SEVERE, "Ошибка валидации XML или оценок", e);
        } catch (EmptyGroupException | IllegalStateException e) {
            System.out.println("Ошибка данных группы: " + e.getMessage());
            logger.log(Level.WARNING, "Нарушение логики данных: {0}", e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода при работе с файлами: " + e.getMessage());
            logger.log(Level.SEVERE, "Ошибка доступа к файлам (XML/XSD/Log)", e);
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка: " + e.getMessage());
            logger.log(Level.SEVERE, "Критическая необработанная ошибка", e);
        } finally {
            logger.info("Завершение основного блока выполнения программы (блок finally).");
        }
    }

    private static void printReport(Group group) {
        System.out.println("Группа: " + group.getName() + ", курс " + group.getCourse());
        System.out.println("----------------------------------------------");
        for (Student s : group.getStudents()) {
            String grades = s.getGrades().stream()
                    .map(Grade::toString)
                    .collect(Collectors.joining(", "));
            System.out.println(s.getId() + ". " + s.getFullName() + " (" + s.getBirthDate() + ")");
            System.out.println("   " + grades);
            System.out.printf("   Средний балл: %.2f%n", s.getAverage());
        }
        System.out.println("----------------------------------------------");
        System.out.printf("Средний балл группы: %.2f%n", group.getAverage());
        Student best = group.getBestStudent();
        if (best != null) {
            System.out.printf("Лучший студент: %s (%.2f)%n", best.getFullName(), best.getAverage());
        }
    }

    private static void printStatistics(Group group, SubjectAnalytics subjectAnalytics, StudentAnalytics studentAnalytics) throws EmptyGroupException {
        System.out.println();
        System.out.println("Статистика по предметам:");
        for (SubjectStats st : subjectAnalytics.bySubject(group).values()) {
            System.out.printf("  %-18s средний %.2f, мин %d, макс %d%n",
                    st.subject(), st.average(), st.min(), st.max());
        }

        System.out.println();
        System.out.println("Распределение оценок:");
        for (Map.Entry<Integer, Integer> e : studentAnalytics.markDistribution(group).entrySet()) {
            System.out.printf("  оценка %d: %d шт.%n", e.getKey(), e.getValue());
        }

        System.out.println();
        System.out.println("Категории студентов:");
        for (Map.Entry<Category, List<Student>> e : studentAnalytics.byCategory(group).entrySet()) {
            String names = e.getValue().isEmpty() ? "—" : e.getValue().stream()
                    .map(Student::getFullName)
                    .collect(Collectors.joining(", "));
            System.out.printf("  %s (%d): %s%n", e.getKey().getTitle(), e.getValue().size(), names);
        }
    }

    private static void printRankings(Group group) throws EmptyGroupException {
        SortStrategy[] strategies = {
                new ByAverageStrategy(),
                new ByLastNameStrategy(),
                new ByDebtsStrategy()
        };
        StudentRanking ranking = new StudentRanking(strategies[0]);

        for (SortStrategy strategy : strategies) {
            ranking.setStrategy(strategy);
            System.out.println();
            System.out.println("Рейтинг " + ranking.getStrategyName() + ":");
            int place = 1;
            for (Student s : ranking.rank(group)) {
                System.out.printf("  %d. %-18s средний %.2f, долгов: %d%n",
                        place++, s.getFullName(), s.getAverage(), s.getDebts().size());
            }
        }
    }
}