package org.software.lms.repository;

import org.software.lms.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    // You can add custom query methods if needed, e.g. find by course, type, etc.
    List<Quiz> findByCourseId(Long courseId);
}
