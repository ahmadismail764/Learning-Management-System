//package org.software.lms.model;
//import jakarta.persistence.*;
//import lombok.*;
//import java.util.Date;
//
//@Entity
//@Table(name = "lesson_attendance")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class LessonAttendance {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "lesson_id", nullable = false)
//    private Lesson lesson;
//
//    @ManyToOne
//    @JoinColumn(name = "student_id", nullable = false)
//    private User student;
//
//    @Column(nullable = false)
//    private Date attendanceTime = new Date();
//}