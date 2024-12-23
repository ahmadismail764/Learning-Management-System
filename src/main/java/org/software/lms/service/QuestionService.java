package org.software.lms.service;

import org.software.lms.dto.QuestionDTO;
import org.software.lms.model.Question;

import java.util.List;

public interface QuestionService {
    Question updateQuestion(Long questionId, Question question);
    QuestionDTO getQuestionById(Long questionId);
    List<Question> getQuestionsByCourse(Long courseId);
    void deleteQuestion(Long questionId);
    QuestionDTO addQuestionToBank(QuestionDTO questionDTO, Long courseId);

}
