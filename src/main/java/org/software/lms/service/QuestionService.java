package org.software.lms.service;

import org.software.lms.dto.QuestionDTO;
import java.util.List;

public interface QuestionService {
    QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO);
    QuestionDTO getQuestionById(Long questionId);
    List<QuestionDTO> getQuestionsByCourse(Long courseId);
    void deleteQuestion(Long questionId);
    QuestionDTO addQuestionToBank(QuestionDTO questionDTO, Long courseId);
}
