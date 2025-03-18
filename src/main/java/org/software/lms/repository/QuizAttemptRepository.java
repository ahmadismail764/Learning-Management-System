package org.software.lms.repository;

import org.software.lms.model.QuizAttempt;
import org.software.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    // Find quiz attempts by student for tracking performance
    List<QuizAttempt> findByStudent(User user);

    // Find quiz attempts by quiz ID to track attempts for a specific quiz
    List<QuizAttempt> findByQuizId(Long quizId);

    List<QuizAttempt> findByQuizCourseIdAndStudentId(Long courseId, Long studentId);

}