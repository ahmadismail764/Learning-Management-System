package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Assignment;
import org.software.lms.model.Course;
import org.software.lms.model.Submission;
import org.software.lms.model.User;
import org.software.lms.repository.AssignmentRepository;
import org.software.lms.repository.CourseRepository;
import org.software.lms.repository.SubmissionRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Manually inject repositories
        ReflectionTestUtils.setField(assignmentService, "assignmentRepository", assignmentRepository);
        ReflectionTestUtils.setField(assignmentService, "submissionRepository", submissionRepository);
        ReflectionTestUtils.setField(assignmentService, "courseRepository", courseRepository);
    }

    @Test
    void testCreateAssignment() {
        // Prepare test data
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);

        Assignment assignment = new Assignment();
        assignment.setTitle("Test Assignment");
        assignment.setDescription("Test Description");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        // Test
        Assignment result = assignmentService.createAssignment(courseId, assignment);

        // Verify
        assertNotNull(result);
        assertEquals("Test Assignment", result.getTitle());
        assertEquals(course, result.getCourse());
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void testCreateAssignment_CourseNotFound() {
        Long courseId = 1L;
        Assignment assignment = new Assignment();

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            assignmentService.createAssignment(courseId, assignment);
        });
    }

    @Test
    void testGetAssignment() {
        Long courseId = 1L;
        Long assignmentId = 1L;
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId))
                .thenReturn(Optional.of(assignment));

        Assignment result = assignmentService.getAssignment(courseId, assignmentId);

        assertNotNull(result);
        assertEquals(assignmentId, result.getId());
    }

    @Test
    void testGetAssignment_NotFound() {
        Long courseId = 1L;
        Long assignmentId = 1L;

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            assignmentService.getAssignment(courseId, assignmentId);
        });
    }

    @Test
    void testUpdateAssignment() {
        Long courseId = 1L;
        Long assignmentId = 1L;
        Assignment existingAssignment = new Assignment();
        existingAssignment.setId(assignmentId);

        Assignment updatedDetails = new Assignment();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setDescription("Updated Description");
        updatedDetails.setDueDate(LocalDateTime.now().plusDays(7));

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId))
                .thenReturn(Optional.of(existingAssignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(updatedDetails);

        Assignment result = assignmentService.updateAssignment(courseId, assignmentId, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void testSubmitAssignment() {
        Long courseId = 1L;
        Long assignmentId = 1L;
        Long studentId = 1L;
        String filePath = "path/to/file";

        Course course = new Course();
        User student = new User();
        student.setId(studentId);
        course.setStudentEnrolledCourses(Arrays.asList(student));

        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setDueDate(LocalDateTime.now().plusDays(1));

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId))
                .thenReturn(Optional.of(assignment));
        when(submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, studentId))
                .thenReturn(false);
        when(submissionRepository.save(any(Submission.class)))
                .thenReturn(new Submission());

        Submission result = assignmentService.submitAssignment(courseId, assignmentId, studentId, filePath);

        assertNotNull(result);
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    void testSubmitAssignment_PastDueDate() {
        Long courseId = 1L;
        Long assignmentId = 1L;
        Long studentId = 1L;
        String filePath = "path/to/file";

        Course course = new Course();
        User student = new User();
        student.setId(studentId);
        course.setStudentEnrolledCourses(Arrays.asList(student));

        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setDueDate(LocalDateTime.now().minusDays(1));

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId))
                .thenReturn(Optional.of(assignment));

        assertThrows(IllegalStateException.class, () -> {
            assignmentService.submitAssignment(courseId, assignmentId, studentId, filePath);
        });
    }

    @Test
    void testGradeSubmission() {
        Long courseId = 1L;
        Long submissionId = 1L;
        Double grade = 85.0;
        String feedback = "Good work";

        Course course = new Course();
        course.setId(courseId);

        Assignment assignment = new Assignment();
        assignment.setCourse(course);

        Submission submission = new Submission();
        submission.setAssignment(assignment);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission result = assignmentService.gradeSubmission(courseId, submissionId, grade, feedback);

        assertNotNull(result);
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    void testGradeSubmission_InvalidGrade() {
        Long courseId = 1L;
        Long submissionId = 1L;
        Double invalidGrade = 101.0;
        String feedback = "Good work";

        Course course = new Course();
        course.setId(courseId);

        Assignment assignment = new Assignment();
        assignment.setCourse(course);

        Submission submission = new Submission();
        submission.setAssignment(assignment);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));

        assertThrows(IllegalArgumentException.class, () -> {
            assignmentService.gradeSubmission(courseId, submissionId, invalidGrade, feedback);
        });
    }

    @Test
    void testGetSubmissionsByAssignment() {
        Long courseId = 1L;
        Long assignmentId = 1L;
        List<Submission> submissions = Arrays.asList(new Submission(), new Submission());

        Assignment assignment = new Assignment();
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId))
                .thenReturn(Optional.of(assignment));
        when(submissionRepository.findByAssignmentId(assignmentId)).thenReturn(submissions);

        List<Submission> result = assignmentService.getSubmissionsByAssignment(courseId, assignmentId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(submissionRepository, times(1)).findByAssignmentId(assignmentId);
    }

    @Test
    void testGetStudentSubmissions() {
        Long courseId = 1L;
        Long studentId = 1L;
        List<Submission> submissions = Arrays.asList(new Submission(), new Submission());

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(submissionRepository.findByAssignmentCourseIdAndStudentId(courseId, studentId))
                .thenReturn(submissions);

        List<Submission> result = assignmentService.getStudentSubmissions(courseId, studentId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteSubmission() {
        Long courseId = 1L;
        Long assignmentId = 1L;
        Long submissionId = 1L;

        Course course = new Course();
        course.setId(courseId);

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setCourse(course);

        Submission submission = new Submission();
        submission.setAssignment(assignment);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));

        assignmentService.deleteSubmission(courseId, assignmentId, submissionId);

        verify(submissionRepository, times(1)).delete(submission);
    }
}
