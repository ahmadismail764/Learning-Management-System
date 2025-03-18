package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.software.lms.dto.QuestionAnswerDTO;
import org.software.lms.dto.QuestionDTO;
import org.software.lms.dto.QuizAttemptDTO;
import org.software.lms.dto.QuizDTO;
import org.software.lms.model.*;
import org.software.lms.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {
    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    private Quiz quiz;
    private Course course;
    private QuizDTO quizDTO;

    @BeforeEach
    void setUp() {
        // Initialize Quiz, Course, and QuizDTO for testing
        course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");

        quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Test Quiz");
        quiz.setDuration(30);
        quiz.setNumberOfQuestions(5);
        quiz.setCourse(course);

        quizDTO = new QuizDTO();
        quizDTO.setId(1L);
        quizDTO.setTitle("Test Quiz DTO");
        quizDTO.setDuration(30);
        quizDTO.setNumberOfQuestions(5);
        quizDTO.setCourseId(1L);
    }

//    @Test
//    void testCreateQuiz() {
//        MockitoAnnotations.openMocks(this);
//
//        // Mock course repository to return a course
//        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
//
//        // Mock quiz repository to save the quiz
//        when(quizRepository.save(ArgumentMatchers.any(Quiz.class))).thenReturn(quiz);
//
//        // Call the service method
//        QuizDTO createdQuiz = quizService.createQuiz(quizDTO, course.getId());//<<<<<<<<<<<<<<<
//
//        // Verify interactions
//        verify(courseRepository).findById(course.getId());
//        verify(quizRepository.save(ArgumentMatchers.any(Quiz.class)));
//
//        // Assert
//        assertNotNull(createdQuiz);
//        assertEquals(quizDTO.getTitle(), createdQuiz.getTitle());
//        assertEquals(quizDTO.getDuration(), createdQuiz.getDuration());
//        assertEquals(quizDTO.getCourseId(), createdQuiz.getCourseId());
//    }

    @Test
    void testGetQuizById() {
        // Mock quiz repository to return a quiz
        when(quizRepository.findByCourseIdAndId(course.getId(), quiz.getId())).thenReturn(Optional.of(quiz));

        // Call the service method
        QuizDTO foundQuiz = quizService.getQuizById(course.getId(), quiz.getId());

        // Verify interactions and assert result
        verify(quizRepository).findByCourseIdAndId(course.getId(), quiz.getId());

        assertNotNull(foundQuiz);
        assertEquals(quiz.getId(), foundQuiz.getId());
        assertEquals(quiz.getTitle(), foundQuiz.getTitle());
    }

    @Test // test for startQuiz
    void testGenerateRandomQuestions() {
        // Mock quiz repository to return a quiz
        when(quizRepository.findByCourseIdAndId(course.getId(), quiz.getId())).thenReturn(Optional.of(quiz));

        // Create a list of questions and mock the question repository to return them
        List<Question> questionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Question question = new Question();
            question.setId((long) i);
            question.setText("Question " + i);
            question.setCourse(course);
            questionList.add(question);
        }
        when(questionRepository.findByCourse(course)).thenReturn(questionList);

        // Call the service method
        List<QuestionDTO> randomQuestions = quizService.generateRandomQuestions(course.getId(), quiz.getId());

        // Verify interactions and assert result
        verify(quizRepository).findByCourseIdAndId(course.getId(), quiz.getId());
        verify(questionRepository).findByCourse(course);

        assertNotNull(randomQuestions);
        assertEquals(quiz.getNumberOfQuestions(), randomQuestions.size());
    }

    @Test
    void testSubmitQuizAttempt() {
        quiz.setNumberOfQuestions(1);
        // Mock quiz, user, and question repositories
        when(quizRepository.findByCourseIdAndId(course.getId(), quiz.getId())).thenReturn(Optional.of(quiz));
        User student = new User();
        student.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));

        List<QuestionAnswerDTO> answers = new ArrayList<>();
        Question question = new Question();
        question.setId(1L);
        question.setCorrectAnswer("Correct");
        question.setCourse(course);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // Answer correctly
        QuestionAnswerDTO answer = new QuestionAnswerDTO();
        answer.setQuestionId(1L);
        answer.setSelectedAnswer("Correct");
        answers.add(answer);

        // Call the service method
        String feedback = quizService.submitQuizAttempt(course.getId(), quiz.getId(), answers, 1L);

        // Verify interactions and assert result
        verify(quizRepository).findByCourseIdAndId(course.getId(), quiz.getId());
        verify(userRepository).findById(1L);
        verify(questionRepository).findById(1L);

        assertNotNull(feedback);
        assertTrue(feedback.contains("passed"));
    }

    @Test
    void testDeleteQuiz() {
        // Mock quiz repository to return a quiz
        when(quizRepository.findByCourseIdAndId(course.getId(), quiz.getId())).thenReturn(Optional.of(quiz));

        // Call the service method
        quizService.deleteQuiz(course.getId(), quiz.getId());

        // Verify interactions
        verify(quizRepository).findByCourseIdAndId(course.getId(), quiz.getId());
        verify(quizRepository).delete(quiz);
    }


}
