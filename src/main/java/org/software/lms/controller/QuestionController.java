package org.software.lms.controller;

import jakarta.validation.Valid;
import org.software.lms.dto.QuestionDTO;
import org.software.lms.dto.UserDto;
import org.software.lms.model.Question;
import org.software.lms.service.QuestionService;
import org.software.lms.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuestionDTO> addQuestionToCourse(@PathVariable Long courseId,
                                                           @Valid @RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestion = questionService.addQuestionToBank(questionDTO, courseId);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByCourse(@PathVariable Long courseId) {
        List<QuestionDTO> questions = questionService.getQuestionsByCourse(courseId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{questionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long courseId,
                                                       @PathVariable Long questionId) {
        QuestionDTO question = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{questionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long courseId,
                                                      @PathVariable Long questionId,
                                                      @Valid @RequestBody QuestionDTO questionDTO) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(questionId, questionDTO);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{questionId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long courseId,
                                               @PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}