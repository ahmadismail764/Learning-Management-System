package org.software.lms.repository;

import org.software.lms.model.Course;
import org.software.lms.model.Question;
import org.software.lms.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    // Find all questions for a specific course (for the question bank)
    List<Question> findByCourseId(Long courseId);
    List<Question> findByCourse(Course course);
}
