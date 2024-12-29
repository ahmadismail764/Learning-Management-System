package org.software.lms.controller;

import org.software.lms.service.PerformanceTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/courses/{courseId}")
public class PerformanceTrackingController {

    @Autowired
    private PerformanceTrackingService performanceTrackingService;

    @GetMapping("/quizzes/{quizId}/performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> getQuizPerformance(
            @PathVariable Long courseId,
            @PathVariable Long quizId) {
        return ResponseEntity.ok(performanceTrackingService.getQuizPerformance(courseId, quizId));
    }

    @GetMapping("/assignments/{assignmentId}/performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> getAssignmentPerformance(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(performanceTrackingService.getAssignmentPerformance(courseId, assignmentId));
    }

    @GetMapping("/students/{studentId}/attendance")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> getAttendancePerformance(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(performanceTrackingService.getAttendancePerformance(courseId, studentId));
    }

    @GetMapping("/students/{studentId}/performance")
    public ResponseEntity<Map<String, Object>> getStudentPerformance(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(performanceTrackingService.getStudentPerformance(courseId, studentId));
    }
}