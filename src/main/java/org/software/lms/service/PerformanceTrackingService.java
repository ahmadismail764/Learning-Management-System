package org.software.lms.service;

import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Course;
import org.software.lms.model.Submission;
import org.software.lms.model.SubmissionStatus;
import org.software.lms.repository.CourseRepository;
import org.software.lms.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceTrackingService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    public double getSubmissionPercentage(Long courseId, Long assignmentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        long totalStudents = course.getStudentEnrolledCourses().size();

        long submittedCount = submissionRepository.findByAssignmentId(assignmentId).size();

        if (totalStudents == 0) {
            return 0.0; // في حالة لم يكن هناك طلاب مسجلين
        }

        double percentage = ((double) submittedCount / totalStudents) * 100;
        return percentage;
    }

}
