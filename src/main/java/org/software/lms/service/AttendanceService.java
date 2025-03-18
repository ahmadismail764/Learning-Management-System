package org.software.lms.service;

import jakarta.transaction.Transactional;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Lesson;
import org.software.lms.model.LessonAttendance;
import org.software.lms.model.User;
import org.software.lms.repository.LessonAttendanceRepository;
import org.software.lms.repository.LessonRepository;
import org.software.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AttendanceService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonAttendanceRepository attendanceRepository;

    private static final long OTP_VALIDITY_PERIOD = 10 * 60 * 1000; // 10 minutes
    private static final int OTP_LENGTH = 6;

    public String generateOTP(Long courseId, Long lessonId) {
        Lesson lesson = lessonRepository.findByIdAndCourse_Id(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        String otp = generateRandomOTP();
        lesson.setCurrentOTP(otp);
        lesson.setOtpGeneratedAt(new Date());
        lessonRepository.save(lesson);

        return otp;
    }

    public LessonAttendance markAttendance(Long courseId, Long lessonId, Long studentId, String otp) {
        Lesson lesson = lessonRepository.findByIdAndCourse_Id(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        validateOTP(lesson, otp);

        if (attendanceRepository.findByLessonAndStudent(lesson, student).isPresent()) {
            throw new IllegalStateException("Attendance already marked for this student");
        }

        LessonAttendance attendance = new LessonAttendance();
        attendance.setLesson(lesson);
        attendance.setStudent(student);
        attendance.setAttendanceDate(new Date());
        attendance.setOtpUsed(otp);
        attendance.setPresent(true);

        return attendanceRepository.save(attendance);
    }

    public List<LessonAttendance> getLessonAttendance(Long courseId, Long lessonId) {
        Lesson lesson = lessonRepository.findByIdAndCourse_Id(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        return attendanceRepository.findByLesson(lesson);
    }

    public List<LessonAttendance> getStudentAttendance(Long courseId, Long lessonId, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return attendanceRepository.findByStudent(student);
    }

    public boolean hasAttendance(Long courseId, Long lessonId, Long studentId) {
        return attendanceRepository.existsByLessonIdAndStudentIdAndPresent(lessonId, studentId, true);
    }

    private String generateRandomOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    private void validateOTP(Lesson lesson, String submittedOTP) {
        String currentOTP = lesson.getCurrentOTP();
        Date otpGeneratedAt = lesson.getOtpGeneratedAt();

        if (currentOTP == null || otpGeneratedAt == null) {
            throw new IllegalStateException("No OTP has been generated for this lesson");
        }

        if (!currentOTP.equals(submittedOTP)) {
            throw new IllegalStateException("Invalid OTP");
        }

        if (System.currentTimeMillis() - otpGeneratedAt.getTime() > OTP_VALIDITY_PERIOD) {
            throw new IllegalStateException("OTP has expired");
        }
    }
}