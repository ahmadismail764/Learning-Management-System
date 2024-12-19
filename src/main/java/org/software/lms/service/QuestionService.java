package org.software.lms.service;

import org.software.lms.model.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Question question);
    Question updateQuestion(Long questionId, Question question);
    Question getQuestionById(Long questionId);
    List<Question> getQuestionsByCourse(Long courseId);
    void deleteQuestion(Long questionId);
}
