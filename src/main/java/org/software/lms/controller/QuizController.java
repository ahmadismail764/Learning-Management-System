package org.software.lms.controller;

import jakarta.validation.Valid;
import org.software.lms.dto.*;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Question;
import org.software.lms.model.Quiz;
import org.software.lms.model.QuizAttempt;
import org.software.lms.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuizDTO> createQuiz(
            @Valid @RequestBody QuizDTO quizDTO,
            @PathVariable Long courseId) {
        QuizDTO createdQuiz = quizService.createQuiz(quizDTO, courseId);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }

    @PostMapping("/{quizId}/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<QuestionDTO>> startQuiz(@PathVariable Long quizId) {
        List<QuestionDTO> createdQuestions = quizService.generateRandomQuestions(quizId);
        return new ResponseEntity<>(createdQuestions, HttpStatus.CREATED);
    }

    @PostMapping("/{quizId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuizAttemptDTO> submitQuiz(
            @PathVariable Long quizId,
            @Valid @RequestBody QuizAttemptDTO submissionDTO) {
        QuizAttemptDTO attemptDTP = quizService.submitQuizAttempt(submissionDTO, quizId);
        return new ResponseEntity<>(attemptDTP, HttpStatus.CREATED);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long quizId) {
        QuizDTO quizDTO = quizService.getQuizById(quizId);
//        return ResponseEntity.ok(quizDTO);
        return new ResponseEntity<>(quizDTO, HttpStatus.CREATED);
    }

//    @PutMapping("/{quizId}")
//    @PreAuthorize("hasRole('INSTRUCTOR')")
//    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz quiz) {
//        Quiz updatedQuiz = quizService.updateQuiz(quizId, quiz);
//        return ResponseEntity.ok(updatedQuiz);
//    }
//
//
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourse(@PathVariable Long courseId) {
        List<Quiz> quizzes = quizService.getQuizzesByCourse(courseId);
        return ResponseEntity.ok(quizzes);
    }

    @DeleteMapping("/{quizId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }
}
