package org.software.lms.service;

import org.software.lms.model.QuizAttempt;

import java.util.List;

public interface QuizAttemptService {
    QuizAttempt createQuizAttempt(QuizAttempt quizAttempt);
    List<QuizAttempt> getQuizAttemptsByStudent(Long studentId);
    List<QuizAttempt> getQuizAttemptsByQuiz(Long quizId);
}
