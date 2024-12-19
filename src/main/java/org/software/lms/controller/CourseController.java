package org.software.lms.controller;

import org.software.lms.dto.CourseDto;
import org.software.lms.model.Course;
import org.software.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        return courseService.updateCourse(id, updatedCourse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @GetMapping("/search/by-title/{title}")
    public List<Course> findCoursesByTitle(@PathVariable String title) {
        return courseService.findCoursesByTitle(title);
    }

    @GetMapping("/search/by-instructor/{instructorId}")
    public List<Course> findCoursesByInstructorId(@PathVariable Long instructorId) {
        return courseService.findCoursesByInstructorId(instructorId);
    }

    @GetMapping("/search/by-created-date/{createdAt}")
    public List<Course> findCoursesByCreatedAtAfter(@PathVariable java.util.Date createdAt) {
        return courseService.findCoursesByCreatedAtAfter(createdAt);
    }
    @PostMapping("/{id}/instructors")
    public ResponseEntity<Course> addInstructorsToCourse(@PathVariable Long id, @RequestBody List<Long> instructorIds) {
        return ResponseEntity.ok(courseService.addInstructorsToCourse(id, instructorIds));
    }

    @PostMapping("/{id}/students")
    public ResponseEntity<Course> addStudentsToCourse(@PathVariable Long id, @RequestBody List<Long> studentIds) {
        return ResponseEntity.ok(courseService.addStudentsToCourse(id, studentIds));
    }

    @PostMapping("/{id}/lessons")
    public ResponseEntity<Course> addLessonsToCourse(@PathVariable Long id, @RequestBody List<Long> lessonIds) {
        return ResponseEntity.ok(courseService.addLessonsToCourse(id, lessonIds));
    }
}

