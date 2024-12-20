package org.software.lms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quizzes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer duration;

    // Automatically set when the entity is persisted
    private LocalDateTime createdAt;

    private Integer numberOfQuestions;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

//    @Column(nullable = false)
//    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Question> questions;

//    @ManyToMany
//    @JoinTable(name = "quiz_questions",
//            joinColumns = @JoinColumn(name = "quiz_id"),
//            inverseJoinColumns = @JoinColumn(name = "question_id"))
//    private List<Question> questions;

    @Column(nullable = false)
    @OneToMany(mappedBy = "quiz")
    private List<QuizAttempt> quizAttempts;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
