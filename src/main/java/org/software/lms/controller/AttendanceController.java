package org.software.lms.controller;

import org.software.lms.model.LessonAttendance;
import org.software.lms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons/{lessonId}/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/generate-otp")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<String> generateOTP(
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        String otp = attendanceService.generateOTP(courseId, lessonId);
        return ResponseEntity.ok(otp);
    }

    @PostMapping("/mark")
    public ResponseEntity<LessonAttendance> markAttendance(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestParam Long studentId,
            @RequestParam String otp) {
        LessonAttendance attendance = attendanceService.markAttendance(courseId, lessonId, studentId, otp);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<LessonAttendance>> getAttendanceForLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        List<LessonAttendance> attendanceList = attendanceService.getLessonAttendance(courseId, lessonId);
        return ResponseEntity.ok(attendanceList);
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<List<LessonAttendance>> getStudentAttendance(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @PathVariable Long studentId) {
        List<LessonAttendance> attendanceList = attendanceService.getStudentAttendance(courseId, lessonId, studentId);
        return ResponseEntity.ok(attendanceList);
    }

    @GetMapping("/students/{studentId}/exists")
    public ResponseEntity<Boolean> hasAttendance(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @PathVariable Long studentId) {
        boolean hasAttendance = attendanceService.hasAttendance(courseId, lessonId, studentId);
        return ResponseEntity.ok(hasAttendance);
    }
}
