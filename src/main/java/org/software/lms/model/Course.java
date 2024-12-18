package org.software.lms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer duration;

    @ManyToMany
    @JoinTable(
            name = "course_instructor",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id")
    )
    private List<User> instructors = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<User> studentEnrolledCourses = new HashSet<>();

    @Column(nullable = false , updatable = false)
    private Date createdAt = new Date();

    @Column(nullable = false)
    private Date updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    public void addInstructor(User instructor) {
        if (this.instructors == null) {
            this.instructors = new ArrayList<>();
        }
        this.instructors.add(instructor);
    }

    public void addLesson(Lesson lesson) {
        if (this.lessons == null) {
            this.lessons = new ArrayList<>();
        }
        this.lessons.add(lesson);
    }

    public void addEnrolledStudent(User student) {
        if (this.studentEnrolledCourses == null) {
            this.studentEnrolledCourses = new HashSet<>();
        }
        this.studentEnrolledCourses.add(student);
    }

    public String getFormattedDuration() {
        int hours = this.duration / 60;
        int minutes = this.duration % 60;
        return String.format("%d hours %d minutes", hours, minutes);
    }

    public boolean hasLessons() {
        return !this.lessons.isEmpty();
    }

    public void updateTimestamp() {
        this.updatedAt = new Date();
    }

    public void removeInstructor(User instructor) {
        if (this.instructors != null) {
            this.instructors.remove(instructor);
        }
    }

    public void removeLesson(Lesson lesson) {
        if (this.lessons != null) {
            this.lessons.remove(lesson);
        }
    }
}