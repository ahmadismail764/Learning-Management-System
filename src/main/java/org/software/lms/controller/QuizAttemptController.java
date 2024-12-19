package org.software.lms.controller;

import org.software.lms.model.QuizAttempt;
import org.software.lms.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-attempts")
public class QuizAttemptController {

    @Autowired
    private QuizAttemptService quizAttemptService;

    @PostMapping
    public ResponseEntity<QuizAttempt> createQuizAttempt(@RequestBody QuizAttempt quizAttempt) {
        QuizAttempt newQuizAttempt = quizAttemptService.createQuizAttempt(quizAttempt);
        return ResponseEntity.ok(newQuizAttempt);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByStudent(@PathVariable Long studentId) {
        List<QuizAttempt> quizAttempts = quizAttemptService.getQuizAttemptsByStudent(studentId);
        return ResponseEntity.ok(quizAttempts);
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByQuiz(@PathVariable Long quizId) {
        List<QuizAttempt> quizAttempts = quizAttemptService.getQuizAttemptsByQuiz(quizId);
        return ResponseEntity.ok(quizAttempts);
    }
}
