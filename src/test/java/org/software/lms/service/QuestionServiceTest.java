package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.software.lms.dto.QuestionDTO;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Course;
import org.software.lms.model.Question;
import org.software.lms.repository.CourseRepository;
import org.software.lms.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddQuestionToBank() {
        Long courseId = 1L;
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setText("Sample Question");

        Course course = new Course();
        course.setId(courseId);

        Question question = new Question();
        question.setText(questionDTO.getText());
        question.setCourse(course);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        QuestionDTO result = questionService.addQuestionToBank(questionDTO, courseId);

        assertNotNull(result);
        assertEquals("Sample Question", result.getText());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testAddQuestionToBank_CourseNotFound() {
        Long courseId = 1L;
        QuestionDTO questionDTO = new QuestionDTO();

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            questionService.addQuestionToBank(questionDTO, courseId);
        });

        verify(courseRepository, times(0)).save(any(Course.class));
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
    }

    @Test
    void testGetQuestionById_QuestionNotFound() {
        Long questionId = 1L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            questionService.getQuestionById(questionId);
        });
    }

    @Test
    void testDeleteQuestion() {
        Long questionId = 1L;
        Course course = new Course();
        Question question = new Question();
        question.setId(questionId);
        question.setCourse(course);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        questionService.deleteQuestion(questionId);

        verify(questionRepository, times(1)).delete(question);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testGetQuestionsByCourse() {
        Long courseId = 1L;
        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setText("Sample Question");
        questions.add(question);

        when(questionRepository.findByCourseId(courseId)).thenReturn(questions);

        List<QuestionDTO> result = questionService.getQuestionsByCourse(courseId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Sample Question", result.get(0).getText());
    }
}
