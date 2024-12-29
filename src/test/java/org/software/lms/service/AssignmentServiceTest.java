package org.software.lms.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.software.lms.model.*;
import org.software.lms.repository.*;
import org.software.lms.service.*;
import org.software.lms.exception.*;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @InjectMocks
    private AssignmentService assignmentService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private CourseRepository courseRepository;

    // @Mock
    // private SubmissionRepository submissionRepository;

    @Test
    void testCreateAssignment() {
        // Mock course repository
        Long courseId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

        // Mock assignment repository
        Assignment assignment = new Assignment();
        assignment.setTitle("Test Assignment");
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        // Call the method
        Assignment result = assignmentService.createAssignment(courseId, assignment);

        // Verify interactions and assertions
        assertNotNull(result);
        assertEquals("Test Assignment", result.getTitle());
        verify(courseRepository, times(1)).findById(courseId);
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }
        @Test
    void testGetAssignment() {
        // Mock course and assignment
        Long courseId = 1L;
        Long assignmentId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        Assignment mockAssignment = new Assignment();
        mockAssignment.setId(assignmentId);
        mockAssignment.setCourse(mockCourse);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(mockAssignment));

        // Call the method
        Assignment result = assignmentService.getAssignment(courseId, assignmentId);

        // Verify interactions and assertions
        assertNotNull(result);
        assertEquals(assignmentId, result.getId());
        verify(courseRepository).findById(courseId);
        verify(assignmentRepository).findByCourseIdAndId(courseId, assignmentId);
    }

    @Test
    void testGetAssignment_CourseNotFound() {
        // Mock assignment
        Long courseId = 1L;
        Long assignmentId = 1L;
        Assignment mockAssignment = new Assignment();
        mockAssignment.setId(assignmentId);
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(mockAssignment));

        // Course not found
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.getAssignment(courseId, assignmentId));
        verify(assignmentRepository, never()).findByCourseIdAndId(courseId, assignmentId);
    }

    @Test
    void testGetAssignment_AssignmentNotFound() {
        // Mock course
        Long courseId = 1L;
        Long assignmentId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

        // Assignment not found
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.getAssignment(courseId, assignmentId));
    }
    @Test
    void testUpdateAssignment() {
        // Mock course and assignment
        Long courseId = 1L;
        Long assignmentId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        Assignment mockAssignment = new Assignment();
        mockAssignment.setId(assignmentId);
        mockAssignment.setCourse(mockCourse);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(mockAssignment));

        // Update details
        Assignment updateDetails = new Assignment();
        updateDetails.setTitle("Updated Title");
        updateDetails.setDescription("Updated Description");

        // Call the method
        Assignment updatedAssignment = assignmentService.updateAssignment(courseId, assignmentId, updateDetails);

        // Verify interactions and assertions
        assertNotNull(updatedAssignment);
        assertEquals("Updated Title", updatedAssignment.getTitle());
        assertEquals("Updated Description", updatedAssignment.getDescription());
        verify(assignmentRepository).findByCourseIdAndId(courseId, assignmentId);
        verify(assignmentRepository).save(updatedAssignment);
    }

    @Test
    void testUpdateAssignment_CourseNotFound() {
        // Mock assignment
        Long courseId = 1L;
        Long assignmentId = 1L;
        Assignment mockAssignment = new Assignment();
        mockAssignment.setId(assignmentId);
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(mockAssignment));

        // Course not found
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.updateAssignment(courseId, assignmentId, new Assignment()));
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void testUpdateAssignment_AssignmentNotFound() {
        // Mock course
        Long courseId = 1L;
        Long assignmentId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

        // Assignment not found
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.updateAssignment(courseId, assignmentId, new Assignment()));
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void testDeleteAssignment() {
        // Mock course and assignment
        Long courseId = 1L;
        Long assignmentId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        Assignment mockAssignment = new Assignment();
        mockAssignment.setId(assignmentId);
        mockAssignment.setCourse(mockCourse);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(mockAssignment));

        // Call the method
        assignmentService.deleteAssignment(courseId, assignmentId);

        // Verify interactions
        verify(assignmentRepository).findByCourseIdAndId(courseId, assignmentId);
        verify(assignmentRepository).delete(mockAssignment);
    }

    @Test
    void testDeleteAssignment_CourseNotFound() {
        // Mock assignment
        Long courseId = 1L;
        Long assignmentId = 1L;
        Assignment mockAssignment = new Assignment();
        mockAssignment.setId(assignmentId);
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.of(mockAssignment));

        // Course not found
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.deleteAssignment(courseId, assignmentId));
        verify(assignmentRepository, never()).delete(any());
    }

    @Test
    void testDeleteAssignment_AssignmentNotFound() {
        // Mock course
        Long courseId = 1L;
        Long assignmentId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

        // Assignment not found
        when(assignmentRepository.findByCourseIdAndId(courseId, assignmentId)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResourceNotFoundException.class, () -> assignmentService.deleteAssignment(courseId, assignmentId));
        verify(assignmentRepository, never()).delete(any());
    }

    @Test
    void testGetAssignmentsByCourse() {
        // Mock course
        Long courseId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

        // Mock assignments
        List<Assignment> mockAssignments = List.of(
                new Assignment(),
                new Assignment()
        );
        when(assignmentRepository.findByCourseId(courseId)).thenReturn(mockAssignments);

        // Call the method
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);

        // Verify interactions and assertions
        assertEquals(2, assignments.size());
        verify(courseRepository).findById(courseId);
        verify(assignmentRepository).findByCourseId(courseId);
    }

}
