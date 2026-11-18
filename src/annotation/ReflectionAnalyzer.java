
package annotation;

import model.Student;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ReflectionAnalyzer {
    private static final Logger logger = Logger.getLogger(ReflectionAnalyzer.class.getName());

    public static void analyzeStudentMetrics(Student sampleStudent) {
        Class<Student> studentClass = Student.class;
        System.out.println("\n=== Reflection API: Анализ аннотаций @AcademicMetric в классе " + studentClass.getSimpleName() + " ===");

        for (Method method : studentClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AcademicMetric.class)) {
                AcademicMetric annotation = method.getAnnotation(AcademicMetric.class);
                System.out.printf("Найден метод: %s() | Приоритет: %d | Описание: %s%n",
                        method.getName(), annotation.priority(), annotation.description());

                if (sampleStudent != null && method.getParameterCount() == 0) {
                    try {
                        Object result = method.invoke(sampleStudent);
                        System.out.println("   -> Результат вызова для " + sampleStudent.getFullName() + ": " + result);
                    } catch (Exception e) {
                        logger.warning("Ошибка рефлексивного вызова метода " + method.getName());
                    }
                }
            }
        }
    }
}