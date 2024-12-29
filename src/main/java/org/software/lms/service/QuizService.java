package org.software.lms.service;

import org.software.lms.dto.QuestionAnswerDTO;
import org.software.lms.dto.QuestionDTO;
import org.software.lms.dto.QuizAttemptDTO;
import org.software.lms.dto.QuizDTO;
import org.software.lms.model.Question;
import org.software.lms.model.Quiz;
import org.software.lms.model.QuizAttempt;

import java.util.List;

public interface QuizService {
    QuizDTO getQuizById(Long courseId, Long quizId);
    List<QuizDTO> getQuizzesByCourse(Long courseId);
    void deleteQuiz(Long courseId, Long quizId);
    QuizDTO createQuiz(QuizDTO quizDTO, Long courseId);
    List<QuestionDTO> generateRandomQuestions(Long courseId, Long quizId);
    String submitQuizAttempt(Long courseId, Long quizId, List<QuestionAnswerDTO> answers, Long studentId);
}
