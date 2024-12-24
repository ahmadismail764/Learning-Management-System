package org.software.lms.dto;

import org.software.lms.model.Course;

public class QuizDTO {
    private Long id;
    private String title;
    private Integer duration;
    private Integer numberOfQuestions;
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer timeLimit) {
        this.duration = timeLimit;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
