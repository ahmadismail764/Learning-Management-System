package org.software.lms.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false , unique = true )
    private String otp;

    @Column(nullable = false)
    private Date otpExpirationTime;

    @ManyToMany
    @JoinTable(
            name = "Lesson_students",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )    private List<User> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonResource> resources = new ArrayList<>();


    public void addAttendance(User student) {
        if (this.attendances == null) {
            this.attendances = new ArrayList<>();
        }
        attendances.add(student);
    }

    public void removeAttendance(User student) {
        if (this.attendances != null) {
            attendances.remove(student);
        }
    }

    public void addResource(LessonResource resource) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }
        resources.add(resource);
        resource.setLesson(this);
    }

    public void removeResource(LessonResource resource) {
        if (this.resources != null) {
            this.resources.remove(resource);
            resource.setLesson(null);
        }
    }

    public boolean isOtpValid() {
        return new Date().before(otpExpirationTime);
    }

    public void updateOtp(String newOtp, Date newExpirationTime) {
        this.otp = newOtp;
        this.otpExpirationTime = newExpirationTime;
    }

//    public Lesson title(String title) {
//        this.title = title;
//        return this;
//    }
//
//    public Lesson description(String description) {
//        this.description = description;
//        return this;
//    }
//
//    public Lesson course(Course course) {
//        this.course = course;
//        return this;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return id != null && id.equals(lesson.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", otp='" + otp + '\'' +
                ", otpExpirationTime=" + otpExpirationTime +
                '}';
    }
}