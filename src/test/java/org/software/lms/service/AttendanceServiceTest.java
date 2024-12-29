//package org.software.lms.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.software.lms.exception.InvalidOTPException;
//import org.software.lms.exception.OTPExpiredException;
//import org.software.lms.exception.ResourceNotFoundException;
//import org.software.lms.model.Lesson;
//import org.software.lms.model.LessonAttendance;
//import org.software.lms.model.User;
//import org.software.lms.repository.LessonAttendanceRepository;
//import org.software.lms.repository.LessonRepository;
//import org.software.lms.repository.UserRepository;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class AttendanceServiceTest {
//
//    @Mock
//    private LessonRepository lessonRepository;
//
//    @Mock
//    private LessonAttendanceRepository attendanceRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private AttendanceService attendanceService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//
//    @Test
//    void testMarkAttendance_LessonNotFound() {
//        Long lessonId = 1L;
//        Long studentId = 2L;
//        String otp = "123456";
//
//        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
//
//        ResourceNotFoundException exception = assertThrows(
//                ResourceNotFoundException.class,
//                () -> attendanceService.markAttendance(lessonId, studentId, otp)
//        );
//
//        assertEquals("Lesson not found with id: 1", exception.getMessage());
//        verify(attendanceRepository, never()).save(any(LessonAttendance.class));
//    }
//
//    @Test
//    void testMarkAttendance_InvalidOTP() {
//        Long lessonId = 1L;
//        Long studentId = 2L;
//        String otp = "123456";
//
//        Lesson lesson = new Lesson();
//        lesson.setId(lessonId);
//        lesson.setCurrentOTP("654321");
//
//        User student = new User();
//        student.setId(studentId);
//
//        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
//        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
//
//        verify(attendanceRepository, never()).save(any(LessonAttendance.class));
//    }
//
//    @Test
//    void testMarkAttendance_OTPExpired() {
//        Long lessonId = 1L;
//        Long studentId = 2L;
//        String otp = "123456";
//
//        Lesson lesson = new Lesson();
//        lesson.setId(lessonId);
//        lesson.setCurrentOTP(otp);
//        lesson.setOtpGeneratedAt(new Date(System.currentTimeMillis() - 31 * 60 * 1000)); // 31 minutes ago
//
//        User student = new User();
//        student.setId(studentId);
//
//        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
//        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
//
//        verify(attendanceRepository, never()).save(any(LessonAttendance.class));
//    }
//
//    @Test
//    void testGetLessonAttendance_Success() {
//        Long lessonId = 1L;
//
//        Lesson lesson = new Lesson();
//        lesson.setId(lessonId);
//
//        List<LessonAttendance> attendances = Arrays.asList(new LessonAttendance(), new LessonAttendance());
//
//        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
//        when(attendanceRepository.findByLesson(lesson)).thenReturn(attendances);
//
//        List<LessonAttendance> result = attendanceService.getLessonAttendance(lessonId);
//
//        assertEquals(2, result.size());
//        verify(attendanceRepository, times(1)).findByLesson(lesson);
//    }
//
//    @Test
//    void testGetStudentAttendance_Success() {
//        Long studentId = 2L;
//
//        User student = new User();
//        student.setId(studentId);
//
//        List<LessonAttendance> attendances = Arrays.asList(new LessonAttendance(), new LessonAttendance());
//
//        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
//        when(attendanceRepository.findByStudent(student)).thenReturn(attendances);
//
//            List<LessonAttendance> result = attendanceService.getStudentAttendance(studentId);
//
//        assertEquals(2, result.size());
//        verify(attendanceRepository, times(1)).findByStudent(student);
//    }
//
//    @Test
//    void testHasAttendance_Success() {
//        Long lessonId = 1L;
//        Long studentId = 2L;
//
//        when(attendanceRepository.existsByLessonIdAndStudentIdAndPresent(lessonId, studentId, true)).thenReturn(true);
//
//            boolean result = attendanceService.hasAttendance(lessonId, studentId);
//
//        assertTrue(result);
//        verify(attendanceRepository, times(1)).existsByLessonIdAndStudentIdAndPresent(lessonId, studentId, true);
//    }
//}
