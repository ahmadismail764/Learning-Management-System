package org.software.lms.service;

import org.software.lms.dto.QuestionDTO;
import org.software.lms.dto.QuizAttemptDTO;
import org.software.lms.dto.QuizDTO;
import org.software.lms.model.Question;
import org.software.lms.model.Quiz;
import org.software.lms.model.QuizAttempt;

import java.util.List;

public interface QuizService {
    QuizDTO getQuizById(Long quizId);

//    Quiz updateQuiz(Long quizId, Quiz quiz);
    List<Quiz> getQuizzesByCourse(Long courseId);

    void deleteQuiz(Long quizId);

    QuizDTO createQuiz(QuizDTO quizDTO, Long courseId);

    List<QuestionDTO> generateRandomQuestions(Long quizId);

    QuizAttemptDTO submitQuizAttempt(QuizAttemptDTO submissionDTO, Long studentId);

//
//    List<QuizAttempt> getQuizAttemptsByQuiz(Long quizId);
//
//    QuizAttempt getQuizAttemptById(Long attemptId);
//
//    List<QuizAttempt> getQuizAttemptsByStudentAndQuiz(Long studentId, Long quizId);
}
