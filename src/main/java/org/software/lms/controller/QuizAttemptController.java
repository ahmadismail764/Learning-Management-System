//package org.software.lms.controller;
//
//import jakarta.validation.Valid;
//import org.software.lms.dto.QuizAttemptDTO;
//import org.software.lms.dto.QuizDTO;
//import org.software.lms.dto.UserDto;
//import org.software.lms.exception.ResourceNotFoundException;
//import org.software.lms.model.Quiz;
//import org.software.lms.model.QuizAttempt;
//import org.software.lms.service.QuizService;
//import org.software.lms.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/quiz-attempts")
//public class QuizAttemptController {
//
//    private final UserService userService;
//    private final QuizService quizService;
//
//    @Autowired
//    public QuizAttemptController(QuizService quizService, UserService userService) {
//        this.quizService = quizService;
//        this.userService = userService;
//    }
//
//    @PostMapping("/submit")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<QuizAttemptDTO> submitQuizAttempt(
//            @Valid @RequestBody QuizAttemptDTO submissionDTO,
//            @AuthenticationPrincipal UserDetails userDetails) {
//        Long studentId = ((UserDto) userDetails).getId();
//
//        // Validate time limit
//        QuizDTO quizDTO = quizService.getQuizById(submissionDTO.getQuizId());
//        Duration timeSpent = Duration.between(submissionDTO.getStartTime(), LocalDateTime.now());
//        if (timeSpent.toMinutes() > quizDTO.getDuration()) {
//            throw new ResourceNotFoundException("Time limit exceeded for this quiz");
//        }
//
//        QuizAttemptDTO attemptDTP = quizService.submitQuizAttempt(submissionDTO, studentId);
//        return new ResponseEntity<>(attemptDTP, HttpStatus.CREATED);
//    }
//
////    @GetMapping("/{attemptId}")
////    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
////    public ResponseEntity<QuizAttemptDTO> getAttemptDetails(@PathVariable Long attemptId) {
////        QuizAttempt attempt = quizService.getQuizAttemptById(attemptId);
////        return ResponseEntity.ok(mapToDetailDTO(attempt));
////    }
//
////    @GetMapping("/student/{studentId}/quiz/{quizId}")
////    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
////    public ResponseEntity<List<QuizAttemptDTO>> getStudentAttempts(
////            @PathVariable Long studentId,
////            @PathVariable Long quizId) {
////        List<QuizAttempt> attempts = quizService.getQuizAttemptsByStudentAndQuiz(studentId, quizId);
////        return ResponseEntity.ok(attempts.stream()
////                .map(this::mapToDetailDTO)
////                .collect(Collectors.toList()));
////    }
//
////    @GetMapping("/quiz/{quizId}/results")
////    @PreAuthorize("hasRole('INSTRUCTOR')")
////    public ResponseEntity<QuizDTO> getQuizResults(@PathVariable Long quizId) {
////        List<QuizAttempt> attempts = quizService.getQuizAttemptsByQuiz(quizId);
////        QuizDTO summary = calculateQuizSummary(attempts);
////        return ResponseEntity.ok(summary);
////    }
////
////    private QuizDTO calculateQuizSummary(List<QuizAttempt> attempts) {
////        return null;
////    }
//
//}
