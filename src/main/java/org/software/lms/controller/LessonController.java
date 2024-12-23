package org.software.lms.controller;

import jakarta.validation.Valid;
import org.software.lms.dto.LessonDTO;
import org.software.lms.model.Lesson;
import org.software.lms.model.LessonResource;
import org.software.lms.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Lesson> createLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody LessonDTO lessonDTO) {
        Lesson createdLesson = lessonService.createLesson(courseId, lessonDTO);
        return ResponseEntity.ok(createdLesson);
    }
    @GetMapping
    public ResponseEntity<List<Lesson>> getLessonsByCourse(@PathVariable Long courseId) {
        List<Lesson> lessons = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        Lesson lesson = lessonService.getLessonById(courseId, lessonId);
        return ResponseEntity.ok(lesson);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonDTO lessonDTO) {
        Lesson updatedLesson = lessonService.updateLesson(courseId, lessonId, lessonDTO);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        lessonService.deleteLesson(courseId, lessonId);
        return ResponseEntity.noContent().build();
    }

    // Resource management endpoints
    @PostMapping("/{lessonId}/resources")
    public ResponseEntity<LessonResource> uploadResource(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestParam("file") MultipartFile file) throws IOException {
        LessonResource resource = lessonService.uploadResource(courseId, lessonId, file);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{lessonId}/resources")
    public ResponseEntity<List<LessonResource>> getLessonResources(
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        List<LessonResource> resources = lessonService.getLessonResources(courseId, lessonId);
        return ResponseEntity.ok(resources);
    }

    @DeleteMapping("/{lessonId}/resources/{resourceId}")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @PathVariable Long resourceId) {
        lessonService.deleteResource(courseId, lessonId, resourceId);
        return ResponseEntity.noContent().build();
    }
}
