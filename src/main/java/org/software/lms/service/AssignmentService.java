package org.software.lms.service;


import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Assignment;
import org.software.lms.model.Submission;
import org.software.lms.repository.AssignmentRepository;
import org.software.lms.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @PreAuthorize("hasRole('ADMIN') OR hasRole('INSTRUCTOR')")
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }
    @PreAuthorize("hasRole('STUDENT')")
    public Submission submitAssignment(Submission submission) {
        return submissionRepository.save(submission);
    }
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public Submission gradeAssignment(Long submissionId, Double grade, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));
        submission.setGrade(grade);
        submission.setFeedback(feedback);
        return submissionRepository.save(submission);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<Submission> getSubmissionsByStudent(Long studentId) {
        return submissionRepository.findByStudentId(studentId);
    }
}
