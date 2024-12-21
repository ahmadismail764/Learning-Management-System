package org.software.lms.repository;
import org.software.lms.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByTitle(String title);
    List<Lesson> findByCourse_Id(Long courseId);
    Optional<Lesson> findByOtp(String otp);
    List<Lesson> findByOtpExpirationTimeAfter(Date otpExpirationTime);
    Optional<Lesson> findById(Long id);
}