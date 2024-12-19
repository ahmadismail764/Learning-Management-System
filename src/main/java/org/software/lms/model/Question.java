package org.software.lms.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

//    @ManyToOne
//    @JoinColumn(name = "course_id", nullable = false)
//    private Course course;

    @ManyToMany(mappedBy = "questions")
    private List<Quiz> quizzes;

}
