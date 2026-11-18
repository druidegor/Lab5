package service;

import exception.EmptyGroupException;
import model.Group;
import model.Student;

import java.util.List;
import java.util.Map;

public interface StudentAnalytics {
    Map<Category, List<Student>> byCategory(Group group) throws EmptyGroupException;
    Map<Integer, Integer> markDistribution(Group group) throws EmptyGroupException;
}
