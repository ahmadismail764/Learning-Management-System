package org.software.lms.service;

import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.*;
import org.software.lms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PerformanceTrackingService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private LessonAttendanceRepository lessonAttendanceRepository;

    // Quiz Performance Methods
    // In PerformanceTrackingService.java
    public Map<String, Object> getQuizPerformance(Long courseId, Long quizId) {
        Quiz quiz = quizRepository.findByCourseIdAndId(courseId, quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        List<QuizAttempt> attempts = quiz.getQuizAttempts();
        Map<String, Object> performance = new HashMap<>();

        // Calculate statistics
        double averageScore = attempts.stream()
                .mapToDouble(attempt -> {
                    // Convert raw score to percentage
                    int totalQuestions = quiz.getNumberOfQuestions();
                    return totalQuestions > 0 ?
                            (attempt.getScore() * 100.0 / totalQuestions) : 0.0;
                })
                .average()
                .orElse(0.0);

        long passedCount = attempts.stream()
                .filter(attempt -> {
                    // Calculate percentage score for pass/fail check
                    int totalQuestions = quiz.getNumberOfQuestions();
                    double percentageScore = totalQuestions > 0 ?
                            (attempt.getScore() * 100.0 / totalQuestions) : 0.0;
                    return percentageScore >= 50;
                })
                .count();

        performance.put("totalAttempts", attempts.size());
        performance.put("averageScore", Math.round(averageScore * 100.0) / 100.0);
        performance.put("passRate", attempts.isEmpty() ? 0 : Math.round((passedCount * 100.0 / attempts.size()) * 100.0) / 100.0);

        return performance;
    }


    public Map<String, Object> getAssignmentPerformance(Long courseId, Long assignmentId) {
        Assignment assignment = assignmentRepository.findByCourseIdAndId(courseId, assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        List<Submission> submissions = assignment.getSubmissions();
        int totalStudents = course.getStudentEnrolledCourses().size();

        Map<String, Object> performance = new HashMap<>();

        // Calculate statistics
        double averageGrade = submissions.stream()
                .map(Submission::getGrade)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        performance.put("totalSubmissions", submissions.size());
        performance.put("submissionRate", totalStudents == 0 ? 0 : (submissions.size() * 100.0 / totalStudents));
        performance.put("averageGrade", averageGrade);

        return performance;
    }

    // Attendance Performance Methods
    public Map<String, Object> getAttendancePerformance(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        List<Lesson> lessons = course.getLessons();
        List<LessonAttendance> attendanceRecords = new ArrayList<>();

        for (Lesson lesson : lessons) {
            attendanceRecords.addAll(lessonAttendanceRepository.findByLesson(lesson));
        }

        Map<String, Object> performance = new HashMap<>();

        long presentCount = attendanceRecords.stream()
                .filter(LessonAttendance::getPresent)
                .count();

        performance.put("totalLessons", lessons.size());
        performance.put("attendedLessons", presentCount);
        performance.put("attendanceRate", lessons.isEmpty() ? 0 : (presentCount * 100.0 / lessons.size()));

        return performance;
    }

    // Overall Student Performance
    public Map<String, Object> getStudentPerformance(Long courseId, Long studentId) {
        Map<String, Object> performance = new HashMap<>();

        // Get quiz performance
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByQuizCourseIdAndStudentId(courseId, studentId);
        double averageQuizScore = quizAttempts.stream()
                .mapToInt(QuizAttempt::getScore)
                .average()
                .orElse(0.0);

        // Get assignment performance
        List<Submission> submissions = submissionRepository.findByAssignmentCourseIdAndStudentId(courseId, studentId);
        double averageAssignmentGrade = submissions.stream()
                .map(Submission::getGrade)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        // Rest of the code remains the same...
        performance.put("averageQuizScore", averageQuizScore);
        performance.put("averageAssignmentGrade", averageAssignmentGrade);
        performance.put("totalQuizzesTaken", quizAttempts.size());
        performance.put("totalAssignmentsSubmitted", submissions.size());

        // Add attendance information
        Map<String, Object> attendanceInfo = getAttendancePerformance(courseId, studentId);
        performance.put("attendance", attendanceInfo);

        return performance;
    }
}