package org.software.lms.service;

import org.software.lms.model.Course;
import org.software.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setTitle(updatedCourse.getTitle());
                    course.setDescription(updatedCourse.getDescription());
                    course.setDuration(updatedCourse.getDuration());
                    course.setLessons(updatedCourse.getLessons());
                    course.setInstructors(updatedCourse.getInstructors());
                    course.updateTimestamp();
                    return courseRepository.save(course);
                }).orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> findCoursesByTitle(String title) {
        return courseRepository.findByTitle(title);
    }

    public List<Course> findCoursesByInstructorId(Long instructorId) {
        return courseRepository.findByInstructors_Id(instructorId);
    }

    public List<Course> findCoursesByCreatedAtAfter(java.util.Date createdAt) {
        return courseRepository.findByCreatedAtAfter(createdAt);
    }
}
