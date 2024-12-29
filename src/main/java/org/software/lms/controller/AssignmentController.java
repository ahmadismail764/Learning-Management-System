package org.software.lms.controller;

import org.software.lms.dto.GradeSubmissionRequest;
import org.software.lms.model.Assignment;
import org.software.lms.model.Submission;
import org.software.lms.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private NotificationController notifControl;

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @PathVariable Long courseId,
            @RequestBody Assignment assignment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.createAssignment(courseId, assignment));
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<Assignment> getAssignment(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getAssignment(courseId, assignmentId));
    }

    @GetMapping
    public ResponseEntity<List<Assignment>> getCourseAssignments(@PathVariable Long courseId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByCourse(courseId));
    }

    @PutMapping("/{assignmentId}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @RequestBody Assignment assignmentDetails) {
        return ResponseEntity.ok(assignmentService.updateAssignment(courseId, assignmentId, assignmentDetails));
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(courseId, assignmentId);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/{assignmentId}/submissions/{submissionId}")
    public ResponseEntity<Submission> updateSubmission(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @PathVariable Long submissionId,
            @RequestParam String filePath) {
        return ResponseEntity.ok(
                assignmentService.updateSubmission(courseId, assignmentId, submissionId, filePath));
    }

    @DeleteMapping("/{assignmentId}/submissions/{submissionId}")
    public ResponseEntity<Void> deleteSubmission(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @PathVariable Long submissionId) {
        assignmentService.deleteSubmission(courseId, assignmentId, submissionId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{assignmentId}/submissions/{submissionId}/grade")
    public ResponseEntity<Submission> gradeSubmission(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @PathVariable Long submissionId,
            @RequestBody GradeSubmissionRequest gradeRequest) {

        Submission submission = assignmentService.gradeSubmission(
                courseId,
                submissionId,
                gradeRequest.getGrade(),
                gradeRequest.getFeedback()
        );

        Long StudId = submission.getStudentId();
        String title = submission.getAssignment().getTitle() + " graded.";
        String message = "Your grade is " + gradeRequest.getGrade();
        notifControl.createNotification(StudId, courseId, title, message);

        return ResponseEntity.ok(submission);
    }

    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<List<Submission>> getAssignmentSubmissions(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getSubmissionsByAssignment(courseId, assignmentId));
    }

    @GetMapping("/submissions/student/{studentId}")
    public ResponseEntity<List<Submission>> getStudentSubmissions(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(assignmentService.getStudentSubmissions(courseId, studentId));
    }
}