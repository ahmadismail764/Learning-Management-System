package org.software.lms.controller;

import org.software.lms.model.Assignment;
import org.software.lms.model.Submission;
import org.software.lms.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Configure this in application.properties
    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        Assignment createdAssignment = assignmentService.createAssignment(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssignment);
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<Submission> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam String filePathOrUrl,
            @RequestParam Long studentId) {

        try {
            // Validate the input (e.g., check if the path/URL is valid)
            if (filePathOrUrl == null || filePathOrUrl.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            // Create a submission object
            Submission submission = new Submission();
            submission.setAssignmentId(assignmentId);
            submission.setStudentId(studentId);
            submission.setFilePath(filePathOrUrl); // Save the path or URL directly

            // Save the submission
            Submission submittedAssignment = assignmentService.submitAssignment(submission);
            return ResponseEntity.status(HttpStatus.CREATED).body(submittedAssignment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        String originalFilename = file.getOriginalFilename();
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        Path targetPath = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath.toString();
    }

    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissionsForAssignment(@PathVariable Long assignmentId) {
        List<Submission> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(submissions);
    }

    @PostMapping("/{submissionId}/grade")
    public ResponseEntity<Submission> gradeAssignment(
            @PathVariable Long submissionId,
            @RequestParam Double grade,
            @RequestParam String feedback) {

        Submission gradedSubmission = assignmentService.gradeAssignment(submissionId, grade, feedback);
        return ResponseEntity.ok(gradedSubmission);
    }
}