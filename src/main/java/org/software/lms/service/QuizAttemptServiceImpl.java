package org.software.lms.service;

import org.software.lms.model.QuizAttempt;
import org.software.lms.repository.QuizAttemptRepository;
import org.software.lms.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Override
    public QuizAttempt createQuizAttempt(QuizAttempt quizAttempt) {
        return quizAttemptRepository.save(quizAttempt);
    }

    @Override
    public List<QuizAttempt> getQuizAttemptsByStudent(Long studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }

    @Override
    public List<QuizAttempt> getQuizAttemptsByQuiz(Long quizId) {
        return quizAttemptRepository.findByQuizId(quizId);
    }
}
