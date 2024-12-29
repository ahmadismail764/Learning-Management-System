package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.software.lms.dto.LessonDTO;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.*;
import org.software.lms.repository.*;
import org.software.lms.service.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonResourceRepository lessonResourceRepository;

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private MultipartFile file;

    private Course course;
    private LessonDTO lessonDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Mock Course
        course = new Course();
        course.setId(1L);
        course.setTitle("Course 1");
        
        // Mock LessonDTO
        lessonDTO = new LessonDTO();
        lessonDTO.setTitle("Lesson 1");
        lessonDTO.setDescription("Lesson description");
        lessonDTO.setDuration(120);
        lessonDTO.setOrderIndex(1);
        
        // Mock file
        file = new MockMultipartFile("testFile", "testFile.pdf", "application/pdf", "content".getBytes());
    }

    @Test
    void testCreateLesson() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(new Lesson());

        Lesson createdLesson = lessonService.createLesson(1L, lessonDTO);

        assertNotNull(createdLesson);
        verify(courseRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void testCreateLesson_CourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lessonService.createLesson(1L, lessonDTO));
    }

    @Test
    void testUploadResource() throws IOException {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        when(lessonRepository.findByIdAndCourse_Id(1L, 1L)).thenReturn(Optional.of(lesson));

        when(lessonResourceRepository.save(any(LessonResource.class))).thenReturn(new LessonResource());

        LessonResource uploadedResource = lessonService.uploadResource(1L, 1L, file);

        assertNotNull(uploadedResource);
        verify(lessonResourceRepository, times(1)).save(any(LessonResource.class));
    }

    @Test
    void testUploadResource_LessonNotFound() throws IOException {
        when(lessonRepository.findByIdAndCourse_Id(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lessonService.uploadResource(1L, 1L, file));
    }

    @Test
    void testGetLessonsByCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        when(lessonRepository.findByCourse_IdOrderByOrderIndexAsc(1L)).thenReturn(new ArrayList<>());

        var lessons = lessonService.getLessonsByCourse(1L);

        assertNotNull(lessons);
        verify(lessonRepository, times(1)).findByCourse_IdOrderByOrderIndexAsc(1L);
    }

    @Test
    void testGetLessonsByCourse_CourseNotFound() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> lessonService.getLessonsByCourse(1L));
    }
}
