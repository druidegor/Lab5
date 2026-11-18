package service;

import exception.EmptyGroupException;
import model.Group;

import java.util.Map;

public interface SubjectAnalytics {
    Map<String, SubjectStats> bySubject(Group group) throws EmptyGroupException;
}
