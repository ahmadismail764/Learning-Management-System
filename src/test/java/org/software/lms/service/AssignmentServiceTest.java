package org.software.lms.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.software.lms.model.*;
import org.software.lms.repository.*;
import org.software.lms.service.*;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
}
