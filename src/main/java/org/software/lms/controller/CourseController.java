package org.software.lms.controller;

import org.software.lms.dto.CourseDto;
import org.software.lms.model.Course;
import org.software.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/search/by-title")
    public List<Course> findCoursesByTitle(@RequestParam String title) {
        return courseService.findCoursesByTitle(title);
    }

    @GetMapping("/search/by-instructor/{instructorId}")
    public List<Course> findCoursesByInstructorId(@PathVariable Long instructorId) {
        return courseService.findCoursesByInstructorId(instructorId);
    }

    @GetMapping("/search/by-created-date")
    public List<Course> findCoursesByCreatedAtAfter(@RequestParam java.util.Date createdAt) {
        return courseService.findCoursesByCreatedAtAfter(createdAt);
    }
}
