package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.software.lms.dto.QuestionDTO;
import org.software.lms.dto.QuizAttemptDTO;
import org.software.lms.dto.QuizDTO;
import org.software.lms.model.*;
import org.software.lms.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private QuestionServiceImpl questionService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addQuestionToBank_Success() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setText("What is Java?");
        questionDTO.setType(QuestionType.MCQ);
        questionDTO.setOptions(Arrays.asList("A", "B", "C", "D"));
        questionDTO.setCorrectAnswer("A");
        questionDTO.setCourse(course);

        Question question = new Question();
        question.setId(1L);
        question.setText(questionDTO.getText());
        question.setType(questionDTO.getType());
        question.setOptions(questionDTO.getOptions());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setCourse(course);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(questionRepository.save(ArgumentMatchers.any(Question.class))).thenReturn(question);

        // Act
        QuestionDTO result = questionService.addQuestionToBank(questionDTO, courseId);

        // Assert
        assertNotNull(result);
        assertEquals(questionDTO.getText(), result.getText());
        assertEquals(questionDTO.getType(), result.getType());
        assertEquals(questionDTO.getOptions(), result.getOptions());
        assertEquals(questionDTO.getCorrectAnswer(), result.getCorrectAnswer());
        assertEquals(course, result.getCourse());

        verify(courseRepository).findById(courseId);
        verify(questionRepository).save(ArgumentMatchers.any(Question.class));
    }


    @Test
    void testUpdateQuestion() {
        Long questionId = 1L;
        Question existingQuestion = new Question();
        existingQuestion.setId(questionId);
        existingQuestion.setText("Old Text");

        Question updatedDetails = new Question();
        updatedDetails.setText("New Text");

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(existingQuestion));
        when(questionRepository.save(ArgumentMatchers.any(Question.class))).thenReturn(updatedDetails);

        Question result = questionService.updateQuestion(questionId, updatedDetails);

        assertNotNull(result);
        assertEquals("New Text", result.getText());
        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, times(1)).save(ArgumentMatchers.any(Question.class));
    }


    @Test
    void testGetQuestionById() {
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);
        question.setText("Sample Question");

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        QuestionDTO result = questionService.getQuestionById(questionId);

        assertNotNull(result);
        assertEquals("Sample Question", result.getText());
        verify(questionRepository, times(1)).findById(questionId);
    }

    @Test
    void testDeleteQuestion() {
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        doNothing().when(questionRepository).deleteById(questionId);

        questionService.deleteQuestion(questionId);

        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, times(1)).deleteById(questionId);
    }


    @Test
    void testGetQuestionsByCourse() {
        Long courseId = 1L;

        Question question1 = new Question();
        question1.setText("Question 1");
        question1.setType(QuestionType.MCQ); // Assuming there's an enum for question type
        question1.setCorrectAnswer("Option A");

        Question question2 = new Question();
        question2.setText("Question 2");
        question2.setType(QuestionType.TRUE_FALSE);
        question2.setCorrectAnswer("True");

        List<Question> questionList = Arrays.asList(question1, question2);

        when(questionRepository.findByCourseId(courseId)).thenReturn(questionList);

        List<Question> result = questionService.getQuestionsByCourse(courseId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Question 1", result.get(0).getText());
        assertEquals("Question 2", result.get(1).getText());
        verify(questionRepository, times(1)).findByCourseId(courseId);
    }




}
