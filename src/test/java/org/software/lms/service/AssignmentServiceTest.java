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
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AssignmentServiceTest {

    @InjectMocks
    private AssignmentService assignmentService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private CourseRepository courseRepository;

    private Course course;
    private Assignment assignment;
    private Submission submission;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock Course
        course = new Course();
        course.setId(1L);

        // Create mock Assignment
        assignment = new Assignment();
        assignment.setId(1L);
        assignment.setCourse(course);
        assignment.setTitle("Test Assignment");
        assignment.setDescription("Test Description");
        assignment.setDueDate(LocalDateTime.now().plusDays(1));

        // Create mock Submission
        submission = new Submission();
        submission.setId(1L);
        submission.setAssignment(assignment);
        submission.setStudentId(123L);
        submission.setFilePath("filePath");
    }

    @Test
    void testCreateAssignment() {
        // Mocking repository save
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        // Calling the service method
        Assignment createdAssignment = assignmentService.createAssignment(1L, assignment);

        // Verifying the results
        assertNotNull(createdAssignment);
        assertEquals("Test Assignment", createdAssignment.getTitle());
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void testGetAssignment() {
        // Mocking repository findByCourseIdAndId
        when(assignmentRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.of(assignment));

        // Calling the service method
        Assignment foundAssignment = assignmentService.getAssignment(1L, 1L);

        // Verifying the results
        assertNotNull(foundAssignment);
        assertEquals("Test Assignment", foundAssignment.getTitle());
    }

    @Test
    void testGetAssignmentNotFound() {
        // Mocking repository to return empty
        when(assignmentRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.empty());

        // Calling the service method and asserting exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.getAssignment(1L, 1L));
    }

    @Test
    void testUpdateAssignment() {
        // Mocking repository findByCourseIdAndId and save
        when(assignmentRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        // Mock new assignment details
        Assignment newDetails = new Assignment();
        newDetails.setTitle("Updated Title");
        newDetails.setDescription("Updated Description");
        newDetails.setDueDate(LocalDateTime.now().plusDays(2));

        // Calling the service method
        Assignment updatedAssignment = assignmentService.updateAssignment(1L, 1L, newDetails);

        // Verifying the results
        assertNotNull(updatedAssignment);
        assertEquals("Updated Title", updatedAssignment.getTitle());
    }

    @Test
    void testDeleteAssignment() {
        // Mocking repository findByCourseIdAndId and delete
        when(assignmentRepository.findByCourseIdAndId(1L, 1L)).thenReturn(Optional.of(assignment));

        // Calling the service method
        assignmentService.deleteAssignment(1L, 1L);

        // Verifying the repository delete method was called
        verify(assignmentRepository, times(1)).delete(assignment);
    }

    @Test
    void testGradeSubmission() {

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission result = assignmentService.gradeSubmission(1L, 1L, 85.0, "Well Done!");

        assertNotNull(result);
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    void testGradeSubmissionNotFound() {
        // Mocking repository to return empty
        when(submissionRepository.findById(1L)).thenReturn(Optional.empty());

        // Calling the service method and asserting exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.gradeSubmission(1L, 1L, 95.0, "Great work!"));
    }

    @Test
    void testSubmitAssignment() {
        String filePath = "path/to/file";

        Course course = new Course();
        User student = new User();
        student.setId(123L);
        course.setStudentEnrolledCourses(Arrays.asList(student));

        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setDueDate(LocalDateTime.now().plusDays(1));

        when(assignmentRepository.findByCourseIdAndId(1L, 1L))
                .thenReturn(Optional.of(assignment));
        when(submissionRepository.existsByAssignmentIdAndStudentId(1L, 123L))
                .thenReturn(false);
        when(submissionRepository.save(any(Submission.class)))
                .thenReturn(new Submission());

        Submission result = assignmentService.submitAssignment(1L, 1L, 123L, filePath);

        assertNotNull(result);
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    void testGetSubmissionsByAssignment() {
        Long courseId = 1L;
        Long assignmentId = 1L;
        List<Submission> submissions = Arrays.asList(new Submission(), new Submission());

        // Mocking repository findByAssignmentId
        when(submissionRepository.findByAssignmentId(1L)).thenReturn(List.of(submission));

        Assignment assignment = new Assignment();
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(assignment));
        when(submissionRepository.findByAssignmentId(assignmentId)).thenReturn(submissions);

        // Calling the service method
        List<Submission> result = assignmentService.getSubmissionsByAssignment(1L, 1L);

        // Verifying the results
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetStudentSubmissions() {
        Long courseId = 1L;

        // Mocking repository findByAssignmentCourseIdAndStudentId
        when(submissionRepository.findByAssignmentCourseIdAndStudentId(1L, 123L)).thenReturn(List.of(submission));

        when(courseRepository.existsById(courseId)).thenReturn(true);

        // Calling the service method
        List<Submission> submissions = assignmentService.getStudentSubmissions(1L, 123L);

        // Verifying the results
        assertNotNull(submissions);
        assertEquals(1, submissions.size());
    }
}
