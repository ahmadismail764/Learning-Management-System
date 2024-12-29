//package org.software.lms.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.software.lms.dto.QuestionDTO;
//import org.software.lms.dto.QuizAttemptDTO;
//import org.software.lms.dto.QuizDTO;
//import org.software.lms.model.*;
//import org.software.lms.repository.*;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.any;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class QuizServiceTest {
//    @Mock
//    private CourseRepository courseRepository;
//
//    @Mock
//    private QuizRepository quizRepository;
//
//    @InjectMocks
//    private QuizServiceImpl quizService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private QuestionRepository questionRepository;
//
//    @Mock
//    private QuizAttemptRepository quizAttemptRepository;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateQuiz() {
//        // Arrange
//        Long courseId = 1L;
//        Course course = new Course();
//        course.setId(courseId);
//
//        QuizDTO quizDTO = new QuizDTO();
//        quizDTO.setTitle("Sample Quiz");
//
//        Quiz quiz = new Quiz();
//        quiz.setId(1L);
//        quiz.setTitle("Sample Quiz");
//        quiz.setDuration(30);
//        quiz.setNumberOfQuestions(5);
//
//        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
////        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);
//        when(quizRepository.save(ArgumentMatchers.any(Quiz.class))).thenReturn(quiz);
//
//        // Act
//        QuizDTO result = quizService.createQuiz(quizDTO, courseId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Sample Quiz", result.getTitle());
//        verify(courseRepository, times(1)).findById(courseId);
////        verify(quizRepository, times(1)).save(any(Quiz.class));
//        verify(quizRepository, times(1)).save(ArgumentMatchers.any(Quiz.class));
//    }
//
//    @Test
//    void testStartQuiz() {
//        // Arrange
//        Long quizId = 1L;
//        Quiz quiz = new Quiz();
//        quiz.setId(quizId);
//        quiz.setNumberOfQuestions(2);
//
//        Course course = new Course();
//        List<Question> questionBank = new ArrayList<>();
//        Question q1 = new Question();
//        q1.setId(1L);
//        questionBank.add(q1);
//        Question q2 = new Question();
//        q2.setId(2L);
//        questionBank.add(q2);
//
//        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
//        when(courseRepository.findByQuizzesId(quizId)).thenReturn(course);
//        when(questionRepository.findByCourse(course)).thenReturn(questionBank);
//
//        // Act
//        List<QuestionDTO> result = quizService.generateRandomQuestions(quizId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(quizRepository, times(1)).findById(quizId);
//        verify(questionRepository, times(1)).findByCourse(course);
//    }
//
//    @Test
//    void testSubmitQuiz() {
//        // Arrange
//        Long quizId = 1L;
//        Long studentId = 1L;
//
//        Quiz quiz = new Quiz();
//        quiz.setId(quizId);
//        quiz.setNumberOfQuestions(5);
//        quiz.setDuration(30); // in minutes
//
//        User student = new User();
//        student.setId(studentId);
//
//        QuizAttemptDTO submissionDTO = new QuizAttemptDTO();
//        submissionDTO.setStudentId(studentId);
//        submissionDTO.setStartTime(LocalDateTime.now());
//        submissionDTO.setEndTime(LocalDateTime.now().plusMinutes(20)); // Within duration
//
//        List<QuestionDTO> answeredQuestions = new ArrayList<>();
//        QuestionDTO questionDTO = new QuestionDTO();
//        questionDTO.setId(1L);
//        questionDTO.setSelectedAnswer("A");
//        answeredQuestions.add(questionDTO);
//        submissionDTO.setAnsweredQuestions(answeredQuestions);
//        submissionDTO.setFeedback(submissionDTO.getStatus() + ", score: " +submissionDTO.getStatus());
//
//        Question question = new Question();
//        question.setId(1L);
//        question.setCorrectAnswer("A");
//
//        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
//        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
//        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
//        when(quizAttemptRepository.save(ArgumentMatchers.any(QuizAttempt.class))).thenReturn((new QuizAttempt()));
//
//
//        // Act
//        QuizAttemptDTO result = quizService.submitQuizAttempt(submissionDTO, quizId);
//
//        // Assert
//        assertNotNull(result);
//        verify(quizRepository, times(1)).findById(quizId);
//        verify(userRepository, times(1)).findById(studentId);
//        verify(questionRepository, times(1)).findById(1L);
//        verify(quizAttemptRepository, times(1)).save(ArgumentMatchers.any(QuizAttempt.class));
//    }
//}
