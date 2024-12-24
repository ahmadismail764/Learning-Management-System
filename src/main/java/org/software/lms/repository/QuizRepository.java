package org.software.lms.repository;

import org.software.lms.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    List<Quiz> findByCourseId(Long courseId);
    @Override
    Optional<Quiz> findById(Long quizId);
    Optional<Quiz> findByCourseIdAndId(Long courseId, Long quizId);

}
