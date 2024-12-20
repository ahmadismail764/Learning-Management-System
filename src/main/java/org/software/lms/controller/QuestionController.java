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
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuestionDTO> addQuestion(
            @Valid @RequestBody QuestionDTO questionDTO,
            @PathVariable Long courseId) {
        QuestionDTO createdQuestion = questionService.addQuestionToBank(questionDTO, courseId);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }


//    @PutMapping("/{questionId}")
//    public ResponseEntity<Question> updateQuestion(@PathVariable Long questionId, @RequestBody Question question) {
//        Question updatedQuestion = questionService.updateQuestion(questionId, question);
//        return ResponseEntity.ok(updatedQuestion);
//    }
//
//    @GetMapping("/{questionId}")
//    public ResponseEntity<Question> getQuestionById(@PathVariable Long questionId) {
//        Question question = questionService.getQuestionById(questionId);
//        return ResponseEntity.ok(question);
//    }
//
//    @GetMapping("/course/{courseId}")
//    public ResponseEntity<List<Question>> getQuestionsByCourse(@PathVariable Long courseId) {
//        List<Question> questions = questionService.getQuestionsByCourse(courseId);
//        return ResponseEntity.ok(questions);
//    }
//
//    @DeleteMapping("/{questionId}")
//    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
//        questionService.deleteQuestion(questionId);
//        return ResponseEntity.noContent().build();
//    }
}
