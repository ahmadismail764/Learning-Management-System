//package org.software.lms.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.software.lms.exception.ResourceNotFoundException;
//import org.software.lms.model.Course;
//import org.software.lms.model.Lesson;
//import org.software.lms.model.LessonResource;
//import org.software.lms.model.ResourceType;
//import org.software.lms.repository.LessonRepository;
//import org.software.lms.repository.LessonResourceRepository;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class LessonServiceTest {
//
//    @Mock
//    private LessonRepository lessonRepository;
//
//    @Mock
//    private LessonResourceRepository lessonResourceRepository;
//
//    private Lesson lesson;
//
//    @BeforeEach
//    void setUp() {
//        lesson = new Lesson();
//        lesson.setId(1L);
//        lesson.setTitle("Sample Lesson");
//        lesson.setDescription("Sample Description");
//        lesson.setCourse(null);  // Simulating a lesson without course for tests
//    }
//
//
//    @Test
//    void testCreateLessonWithNullCourse() {
//        lesson.setCourse(null);
//
//        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//            lessonService.createLesson(lesson);
//        });
//
//        assertEquals("Lesson must be associated with a course", thrown.getMessage());
//    }
//
//    @Test
//    void testUpdateLesson() {
//        Lesson updatedDetails = new Lesson();
//        updatedDetails.setTitle("Updated Title");
//        updatedDetails.setDescription("Updated Description");
//
//        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
//        when(lessonRepository.save(lesson)).thenReturn(lesson);
//
//        Lesson updatedLesson = lessonService.updateLesson(1L, updatedDetails);
//
//        assertEquals("Updated Title", updatedLesson.getTitle());
//        assertEquals("Updated Description", updatedLesson.getDescription());
//        verify(lessonRepository, times(1)).save(lesson);
//    }
//
//    @Test
//    void testGenerateOTP() {
//        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
//
//        String otp = lessonService.generateOTP(1L);
//
//        assertNotNull(otp);
//        assertEquals(6, otp.length());
//        verify(lessonRepository, times(1)).save(lesson);
//    }
//
//    @Test
//    void testValidateOTP_Valid() {
//        lesson.setCurrentOTP("123456");
//        lesson.setOtpGeneratedAt(new Date());
//        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
//
//        boolean isValid = lessonService.validateOTP(1L, "123456");
//
//        assertTrue(isValid);
//    }
//
//    @Test
//    void testValidateOTP_Invalid() {
//        lesson.setCurrentOTP("123456");
//        lesson.setOtpGeneratedAt(new Date());
//        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
//
//        boolean isValid = lessonService.validateOTP(1L, "654321");
//
//        assertFalse(isValid);
//    }
//
//    @Test
//    void testGetLessonResources() {
//        LessonResource resource1 = new LessonResource();
//        resource1.setFileName("file1.pdf");
//        LessonResource resource2 = new LessonResource();
//        resource2.setFileName("file2.mp4");
//
//        lesson.setLessonResources(Arrays.asList(resource1, resource2));
//        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
//
//        List<LessonResource> resources = lessonService.getLessonResources(1L);
//
//        assertEquals(2, resources.size());
//        assertEquals("file1.pdf", resources.get(0).getFileName());
//        assertEquals("file2.mp4", resources.get(1).getFileName());
//    }
//
//    @Test
//    void testDeleteLesson() {
//        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
//        doNothing().when(lessonRepository).delete(lesson);
//
//        lessonService.deleteLesson(1L);
//
//        verify(lessonRepository, times(1)).delete(lesson);
//    }
//
//    @Test
//    void testLessonNotFound() {
//        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());
//
//        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
//            lessonService.updateLesson(1L, lesson);
//        });
//
//        assertEquals("Lesson not found with id: 1", thrown.getMessage());
//    }
//}
