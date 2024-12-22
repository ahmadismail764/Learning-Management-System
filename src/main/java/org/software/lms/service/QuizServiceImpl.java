package org.software.lms.service;

import jakarta.transaction.Transactional;
import org.software.lms.dto.QuestionDTO;
import org.software.lms.dto.QuizAttemptDTO;
import org.software.lms.dto.QuizDTO;
import org.software.lms.dto.UserDto;
import org.software.lms.exception.ResourceNotFoundException;
import org.software.lms.model.*;
import org.software.lms.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {//<<<<<<<<<<<<<<<<<<<<<

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuizServiceImpl(QuizRepository quizRepository,
                           QuestionRepository questionRepository,
                           CourseRepository courseRepository,
                           QuizAttemptRepository quizAttemptRepository,
                           UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.courseRepository = courseRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public QuizDTO getQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        QuizDTO quizDTO = new QuizDTO();
        BeanUtils.copyProperties(quiz, quizDTO);

        return quizDTO;
    }

    @Override
    public QuizDTO createQuiz(QuizDTO quizDTO, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        quizDTO.setCourse(course);
        Quiz quiz = new Quiz();
        BeanUtils.copyProperties(quizDTO, quiz);

        Quiz savedQuiz = quizRepository.save(quiz);

        QuizDTO savedQuizDto = new QuizDTO();
        BeanUtils.copyProperties(savedQuiz, savedQuizDto);

        return savedQuizDto;
    }

    @Override
    public List<QuestionDTO> generateRandomQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        Course course = courseRepository.findByQuizzesId(quizId);
        List<Question> questionBank = questionRepository.findByCourse(course);

        if (questionBank.size() < quiz.getNumberOfQuestions()) {
            throw new IllegalStateException("Not enough questions in the bank");
        }

        Collections.shuffle(questionBank);
        List<QuestionDTO> generatedQuestionsDTOs = new ArrayList<>();

        for (int i = 0; i < quiz.getNumberOfQuestions(); i++) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(questionBank.get(i), questionDTO);
            generatedQuestionsDTOs.add(questionDTO);
        }
        return generatedQuestionsDTOs;
    }

    @Override
    public QuizAttemptDTO submitQuizAttempt(QuizAttemptDTO submissionDTO, Long studentId) {
        Quiz quiz = quizRepository.findById(submissionDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        QuizAttempt attempt = new QuizAttempt();
        BeanUtils.copyProperties(submissionDTO, attempt);

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

        QuizAttemptDTO savedAttemptDto = new QuizAttemptDTO();
        BeanUtils.copyProperties(savedAttempt, savedAttemptDto);

        return savedAttemptDto;
    }

//    public float calculateScore(List<QuestionDTO> answers) {
//        float correctAnswers = 0;
//        for (QuestionDTO answer : answers) {
//            Question question = questionRepository.findById(answer.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
//
//            if (question.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
//                correctAnswers++;
//            }
//        }
//        return (correctAnswers / answers.size()) * 100;
//    }

//    private List<Question> mapAnswers(List<QuestionDTO> answerDTOs) {
//        return answerDTOs.stream()
//                .map(dto -> {
//                    Question answer = new Question();
//                    answer.getId(questionRepository.findById(dto.getId())
//                            .orElseThrow(() -> new ResourceNotFoundException("Question not found")));
//                    answer.setSelectedAnswer(dto.getSelectedAnswer());
//                    return answer;
//                })
//                .collect(Collectors.toList());
//    }


//    @Override
//    public Quiz updateQuiz(Long quizId, Quiz quizDetails) {
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id " + quizId));
//
//        quiz.setDuration(quizDetails.getDuration());
//        return quizRepository.save(quiz);
//    }
//
//    @Override
//    public Quiz getQuizById(Long quizId) {
//        return quizRepository.findById(quizId)
//                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id " + quizId));
//    }
//
//    @Override
//    public List<Quiz> getQuizzesByCourse(Long courseId) {
//        return quizRepository.findByCourseId(courseId);
//    }
//
//    @Override
//    public void deleteQuiz(Long quizId) {
//        quizRepository.deleteById(quizId);
//    }
}
