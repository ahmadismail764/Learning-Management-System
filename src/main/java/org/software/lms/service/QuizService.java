package org.software.lms.service;

import org.software.lms.model.Quiz;

import java.util.List;

public interface QuizService {
    Quiz createQuiz(Quiz quiz);
    Quiz updateQuiz(Long quizId, Quiz quiz);
    Quiz getQuizById(Long quizId);
    List<Quiz> getQuizzesByCourse(Long courseId);
    void deleteQuiz(Long quizId);
}
