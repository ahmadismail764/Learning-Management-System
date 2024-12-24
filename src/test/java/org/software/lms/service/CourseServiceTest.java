package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.software.lms.model.Course;
import org.software.lms.model.Lesson;
import org.software.lms.model.Role;
import org.software.lms.model.User;
import org.software.lms.repository.CourseRepository;
import org.software.lms.repository.LessonRepository;
import org.software.lms.repository.UserRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse() {
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Course Description");

        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course createdCourse = courseService.createCourse(course);

        assertNotNull(createdCourse);
        assertEquals("Test Course", createdCourse.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course();
        Course course2 = new Course();

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCourseById_Success() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Optional<Course> fetchedCourse = courseService.getCourseById(courseId);

        assertTrue(fetchedCourse.isPresent());
        assertEquals(courseId, fetchedCourse.get().getId());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testGetCourseById_NotFound() {
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Optional<Course> fetchedCourse = courseService.getCourseById(courseId);

        assertFalse(fetchedCourse.isPresent());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testUpdateCourse() {
        Long courseId = 1L;
        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setTitle("Old Title");

        Course updatedCourse = new Course();
        updatedCourse.setTitle("Updated Title");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        Course updated = courseService.updateCourse(courseId, updatedCourse);

        assertEquals("Updated Title", updated.getTitle());
        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    void testDeleteCourse() {
        Long courseId = 1L;

        doNothing().when(courseRepository).deleteById(courseId);

        courseService.deleteCourse(courseId);

        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    void testFindCoursesByTitle() {
        String title = "Course Title";
        Course course = new Course();
        course.setTitle(title);

        when(courseRepository.findByTitle(title)).thenReturn(Arrays.asList(course));

        List<Course> courses = courseService.findCoursesByTitle(title);

        assertEquals(1, courses.size());
        assertEquals(title, courses.get(0).getTitle());
        verify(courseRepository, times(1)).findByTitle(title);
    }

    @Test
    void testFindCoursesByInstructorId() {
        Long instructorId = 1L;
        Course course = new Course();

        when(courseRepository.findByInstructors_Id(instructorId)).thenReturn(Arrays.asList(course));

        List<Course> courses = courseService.findCoursesByInstructorId(instructorId);

        assertEquals(1, courses.size());
        verify(courseRepository, times(1)).findByInstructors_Id(instructorId);
    }

    @Test
    void testFindCoursesByCreatedAtAfter() {
        Date createdAt = new Date();
        Course course = new Course();

        when(courseRepository.findByCreatedAtAfter(createdAt)).thenReturn(Arrays.asList(course));

        List<Course> courses = courseService.findCoursesByCreatedAtAfter(createdAt);

        assertEquals(1, courses.size());
        verify(courseRepository, times(1)).findByCreatedAtAfter(createdAt);
    }

}
