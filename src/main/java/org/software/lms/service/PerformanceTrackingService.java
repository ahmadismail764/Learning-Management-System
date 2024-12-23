package org.software.lms.service;

import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Assignment;
import org.software.lms.model.Course;
import org.software.lms.repository.AssignmentRepository;
import org.software.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PerformanceTrackingService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public double getSubmissionPercentage(Long courseId, Long assignmentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        long totalStudents = course.getStudentEnrolledCourses().size();

        Optional<Assignment> assignment = assignmentRepository.findByCourseIdAndId(courseId,assignmentId);
        long submittedCount;
        if (assignment.isPresent()) submittedCount = assignment.get().getSubmissions().size();
        else submittedCount = 0;

        if (totalStudents == 0) {
            return 0.0;
        }

        double percentage = ((double) submittedCount / totalStudents) * 100;
        return percentage;
    }
    public long getTotalSubmissions(Long courseId, Long assignmentId) {
        Optional<Assignment> assignment = assignmentRepository.findByCourseIdAndId(courseId,assignmentId);
        return (assignment.isPresent())?  assignment.get().getSubmissions().size() : 0;
    }

}
