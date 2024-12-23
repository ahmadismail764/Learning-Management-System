package org.software.lms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Assignment;
import org.software.lms.model.Course;
import org.software.lms.model.Submission;
import org.software.lms.repository.AssignmentRepository;
import org.software.lms.repository.CourseRepository;
import org.software.lms.repository.SubmissionRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    }

    @Test
    void testCreateAssignment_Success() {
        Long courseId = 1L;
        Assignment assignment = new Assignment();
        assignment.setTitle("Test Assignment");

        Course course = new Course();
        course.setId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        Assignment createdAssignment = assignmentService.createAssignment(courseId, assignment);

        assertNotNull(createdAssignment);
        assertEquals("Test Assignment", createdAssignment.getTitle());
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void testCreateAssignment_CourseNotFound() {
        Long courseId = 1L;
        Assignment assignment = new Assignment();

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> assignmentService.createAssignment(courseId, assignment)
        );

        assertEquals("Course not found with id: 1", exception.getMessage());
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

    @Test
    void testGetAssignment_Success() {
        Long courseId = 1L;
        Long assignmentId = 2L;

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setCourse(new Course());

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(assignment));

        Assignment fetchedAssignment = assignmentService.getAssignment(courseId, assignmentId);

        assertNotNull(fetchedAssignment);
        assertEquals(assignmentId, fetchedAssignment.getId());
        verify(assignmentRepository, times(1)).findByCourseIdAndId(courseId, assignmentId);
    }

    @Test
    void testGetAssignment_NotFound() {
        Long courseId = 1L;
        Long assignmentId = 2L;

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> assignmentService.getAssignment(courseId, assignmentId)
        );

        assertEquals("Assignment not found", exception.getMessage());
        verify(assignmentRepository, times(1)).findByCourseIdAndId(courseId, assignmentId);
    }

    @Test
    void testUpdateAssignment_Success() {
        Long courseId = 1L;
        Long assignmentId = 2L;

        Assignment existingAssignment = new Assignment();
        existingAssignment.setId(assignmentId);

        Assignment updatedDetails = new Assignment();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setDescription("Updated Description");

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(existingAssignment));
        when(assignmentRepository.save(existingAssignment)).thenReturn(existingAssignment);

        Assignment updatedAssignment = assignmentService.updateAssignment(courseId, assignmentId, updatedDetails);

        assertEquals("Updated Title", updatedAssignment.getTitle());
        assertEquals("Updated Description", updatedAssignment.getDescription());
        verify(assignmentRepository, times(1)).save(existingAssignment);
    }

    @Test
    void testDeleteAssignment_Success() {
        Long courseId = 1L;
        Long assignmentId = 2L;

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(assignment));

        assignmentService.deleteAssignment(courseId, assignmentId);

        verify(assignmentRepository, times(1)).delete(assignment);
    }

    @Test
    void testGetAssignmentsByCourse_Success() {
        Long courseId = 1L;

        Assignment assignment1 = new Assignment();
        Assignment assignment2 = new Assignment();

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(assignmentRepository.findByCourseId(courseId)).thenReturn(Arrays.asList(assignment1, assignment2));

        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);

        assertEquals(2, assignments.size());
        verify(assignmentRepository, times(1)).findByCourseId(courseId);
    }

}
