package org.software.lms.service;

import org.software.lms.dto.QuestionDTO;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.Course;
import org.software.lms.model.Question;
import org.software.lms.repository.CourseRepository;
import org.software.lms.repository.QuestionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto, "course");
        if (question.getCourse() != null) {
            dto.setCourseId(question.getCourse().getId());
        }
        return dto;
    }

    private Question convertToEntity(QuestionDTO dto, Course course) {
        Question question = new Question();
        BeanUtils.copyProperties(dto, question, "courseId");
        question.setCourse(course);
        return question;
    }

    @Override
    @Transactional
    public QuestionDTO addQuestionToBank(QuestionDTO questionDTO, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseId));

        Question question = convertToEntity(questionDTO, course);
        Question savedQuestion = questionRepository.save(question);

        course.addQuestionToBank(savedQuestion);
        courseRepository.save(course);

        return convertToDTO(savedQuestion);
    }

    @Override
    @Transactional
    public QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));

        Course course = existingQuestion.getCourse();
        if (course == null) {
            throw new ResourceNotFoundException("Question is not associated with any course");
        }

        // Remove from old position in question bank
        course.getQuestionBank().remove(existingQuestion);

        // Update fields
        existingQuestion.setText(questionDTO.getText());
        existingQuestion.setType(questionDTO.getType());
        existingQuestion.setOptions(questionDTO.getOptions());
        existingQuestion.setCorrectAnswer(questionDTO.getCorrectAnswer());
        existingQuestion.setSelectedAnswer(questionDTO.getSelectedAnswer());

        // Save question
        Question updatedQuestion = questionRepository.save(existingQuestion);

        // Add back to question bank in new position
        course.addQuestionToBank(updatedQuestion);
        courseRepository.save(course);

        return convertToDTO(updatedQuestion);
    }


    @Override
    public QuestionDTO getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
        return convertToDTO(question);
    }


    @Override
    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));

        Course course = question.getCourse();
        if (course != null) {
            // Use the helper method from Course entity
            course.removeQuestionFromBank(question);
            courseRepository.save(course);
        }

        // Finally delete the question
        questionRepository.delete(question);
    }

    @Override
    public List<QuestionDTO> getQuestionsByCourse(Long courseId) {
        List<Question> questions = questionRepository.findByCourseId(courseId);
        return questions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}