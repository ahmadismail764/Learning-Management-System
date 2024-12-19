package org.software.lms.repository;

import org.software.lms.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    // Find quiz attempts by student ID for tracking performance
    List<QuizAttempt> findByStudentId(Long studentId);

    // Find quiz attempts by quiz ID to track attempts for a specific quiz
    List<QuizAttempt> findByQuizId(Long quizId);
}