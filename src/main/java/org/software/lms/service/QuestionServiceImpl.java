package org.software.lms.service;

import org.software.lms.dto.QuestionDTO;
import org.software.lms.dto.UserDto;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Course;
import org.software.lms.model.Question;
import org.software.lms.model.User;
import org.software.lms.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository,
                               CourseRepository courseRepository) {
        this.questionRepository = questionRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public QuestionDTO addQuestionToBank(QuestionDTO questionDTO, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        questionDTO.setCourse(course);
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);

        Question savedQuestion = questionRepository.save(question);

        QuestionDTO savedQuestionDto = new QuestionDTO();
        BeanUtils.copyProperties(savedQuestion, savedQuestionDto);

        course.addQuestionToBank(question);

        return savedQuestionDto;
    }

    @Override
    public Question updateQuestion(Long questionId, Question questionDetails) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));

        question.setText(questionDetails.getText());
//        question.setAnswerOptions(questionDetails.getAnswerOptions());
        return questionRepository.save(question);
    }

    @Override
    public QuestionDTO getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        return questionDTO;
    }

    @Override
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
        questionRepository.deleteById(questionId);
    }


    @Override
    public List<Question> getQuestionsByCourse(Long courseId) {
        return questionRepository.findByCourseId(courseId);
    }

}
