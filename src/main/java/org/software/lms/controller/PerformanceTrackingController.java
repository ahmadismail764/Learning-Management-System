package org.software.lms.controller;

import org.software.lms.service.PerformanceTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses/performance")
public class PerformanceTrackingController {

    @Autowired
    private PerformanceTrackingService performanceTrackingService;

    @GetMapping("/submission-percentage/{courseId}/{assignmentId}")
    public ResponseEntity<Double> getSubmissionPercentage(@PathVariable Long courseId, @PathVariable Long assignmentId) {
        double percentage = performanceTrackingService.getSubmissionPercentage(courseId, assignmentId);
        return ResponseEntity.ok(percentage);
    }
    @GetMapping("/Total-submission/{courseId}/{assignmentId}")
    public ResponseEntity<Long> getTotalSubmission(@PathVariable Long courseId, @PathVariable Long assignmentId) {
        Long percentage = performanceTrackingService.getTotalSubmissions(courseId, assignmentId);
        return ResponseEntity.ok(percentage);
    }

}
