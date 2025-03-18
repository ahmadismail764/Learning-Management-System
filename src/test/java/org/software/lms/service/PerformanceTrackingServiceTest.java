package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.*;
import org.software.lms.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PerformanceTrackingServiceTest {

    @InjectMocks
    private PerformanceTrackingService performanceTrackingService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private LessonAttendanceRepository lessonAttendanceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQuizPerformance() {
        // Arrange
        Long courseId = 1L;
        Long quizId = 1L;

        Quiz quiz = new Quiz();
        quiz.setNumberOfQuestions(10);

        QuizAttempt attempt1 = new QuizAttempt();
        attempt1.setScore(8);

        QuizAttempt attempt2 = new QuizAttempt();
        attempt2.setScore(6);

        quiz.setQuizAttempts(Arrays.asList(attempt1, attempt2));

        when(quizRepository.findByCourseIdAndId(courseId, quizId)).thenReturn(Optional.of(quiz));

        // Act
        Map<String, Object> performance = performanceTrackingService.getQuizPerformance(courseId, quizId);

        // Assert
        assertNotNull(performance);
        assertEquals(2, performance.get("totalAttempts"));
        assertEquals(70.0, performance.get("averageScore"));
        assertEquals(100.0, performance.get("passRate"));
        verify(quizRepository).findByCourseIdAndId(courseId, quizId);
    }

    @Test
    void testGetQuizPerformance_QuizNotFound() {
        // Arrange
        Long courseId = 1L;
        Long quizId = 1L;

        when(quizRepository.findByCourseIdAndId(courseId, quizId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> performanceTrackingService.getQuizPerformance(courseId, quizId));
        verify(quizRepository).findByCourseIdAndId(courseId, quizId);
    }

    @Test
    void testGetAssignmentPerformance() {
        // Arrange
        Long courseId = 1L;
        Long assignmentId = 1L;

        Assignment assignment = new Assignment();
        Submission submission1 = new Submission();
        submission1.setGrade(85.0);

        Submission submission2 = new Submission();
        submission2.setGrade(90.0);

        assignment.setSubmissions(Arrays.asList(submission1, submission2));

        Course course = new Course();
        course.setStudentEnrolledCourses(Arrays.asList(new User(), new User(), new User())); // 3 students enrolled

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(assignment));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        Map<String, Object> performance = performanceTrackingService.getAssignmentPerformance(courseId, assignmentId);

        // Assert
        assertNotNull(performance);
        assertEquals(2, performance.get("totalSubmissions"));
        assertEquals(66.67, Math.round((double) performance.get("submissionRate") * 100.0) / 100.0);
        assertEquals(87.5, performance.get("averageGrade"));
        verify(assignmentRepository).findByCourseIdAndId(courseId, assignmentId);
        verify(courseRepository).findById(courseId);
    }

    @Test
    void testGetAttendancePerformance() {
        // Arrange
        Long courseId = 1L;
        Long studentId = 1L;

        Course course = new Course();
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        course.setLessons(Arrays.asList(lesson1, lesson2)); // Set lessons for the course

        LessonAttendance attendance1 = new LessonAttendance();
        attendance1.setPresent(true); // Student attended lesson1

        LessonAttendance attendance2 = new LessonAttendance();
        attendance2.setPresent(false); // Student did not attend lesson2

        // Mock repository responses
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonAttendanceRepository.findByLesson(lesson1)).thenReturn(Collections.singletonList(attendance1));
        when(lessonAttendanceRepository.findByLesson(lesson2)).thenReturn(Collections.singletonList(attendance2));

        // Act
        Map<String, Object> performance = performanceTrackingService.getAttendancePerformance(courseId, studentId);

        // Assert
        assertNotNull(performance); // Ensure the performance map is not null
        assertEquals(2, performance.get("totalLessons")); // Total lessons should be 2
        assertEquals(1L, performance.get("attendedLessons")); // Student attended 1 lesson
        assertEquals(50.0, performance.get("attendanceRate")); // Attendance rate should be 50%

        // Verify repository calls
        verify(courseRepository).findById(courseId);
        verify(lessonAttendanceRepository).findByLesson(lesson1);
        verify(lessonAttendanceRepository).findByLesson(lesson2);
    }


    @Test
    void testGetStudentPerformance() {
        // Arrange
        Long courseId = 1L;
        Long studentId = 1L;

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setScore(80);

        Submission submission = new Submission();
        submission.setGrade(90.0);

        when(quizAttemptRepository.findByQuizCourseIdAndStudentId(courseId, studentId)).thenReturn(Arrays.asList(quizAttempt));
        when(submissionRepository.findByAssignmentCourseIdAndStudentId(courseId, studentId)).thenReturn(Arrays.asList(submission));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course()));
        when(lessonAttendanceRepository.findByLesson(any(Lesson.class))).thenReturn(Collections.emptyList());

        // Act
        Map<String, Object> performance = performanceTrackingService.getStudentPerformance(courseId, studentId);

        // Assert
        assertNotNull(performance);
        assertEquals(80.0, performance.get("averageQuizScore"));
        assertEquals(90.0, performance.get("averageAssignmentGrade"));
        assertEquals(1, performance.get("totalQuizzesTaken"));
        assertEquals(1, performance.get("totalAssignmentsSubmitted"));
        verify(quizAttemptRepository).findByQuizCourseIdAndStudentId(courseId, studentId);
        verify(submissionRepository).findByAssignmentCourseIdAndStudentId(courseId, studentId);
    }
}
