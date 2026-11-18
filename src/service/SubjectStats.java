package service;

// Статистика по одному предмету (record — неизменяемый класс-хранилище данных)
public record SubjectStats(
        String subject,
        double average,
        int min,
        int max,
        int count
) {
}
