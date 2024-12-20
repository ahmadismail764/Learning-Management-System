package org.software.lms.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuizAttemptDTO {
    private Long id;
    private Long quizId;
    private Long studentId;
    private float score;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<QuestionDTO> answers;
    int timeSpentMinutes;
    String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<QuestionDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionDTO> answers) {
        this.answers = answers;
    }

    public void setTimeSpentMinutes(int timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }

    public int getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
