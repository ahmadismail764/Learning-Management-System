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

import java.time.Duration;
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
    public QuizAttemptDTO submitQuizAttempt(QuizAttemptDTO submissionDTO, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        // Validate time limit
        Duration timeSpent = Duration.between(submissionDTO.getStartTime(), submissionDTO.getEndTime());
        if (timeSpent.toMinutes() > quiz.getDuration()) {
            throw new ResourceNotFoundException("Time limit exceeded for this quiz");
        }

        User student = userRepository.findById(submissionDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<QuestionDTO> studentAnswers = submissionDTO.getAnsweredQuestions();
        int score = 0;

        List<Question> questions = new ArrayList<>();
        for (QuestionDTO answerDTO : studentAnswers) {
            // Fetch the original question from the database
            Question question = questionRepository.findById(answerDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + answerDTO.getId()));

            // Only set the selected answer, don't modify the original question
            question.setSelectedAnswer(answerDTO.getSelectedAnswer());

            // Compare answers for scoring
            if (answerDTO.getSelectedAnswer() != null &&
                    answerDTO.getSelectedAnswer().equals(question.getCorrectAnswer())) {
                score++;
            }

            questions.add(question);
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudent(student);
        attempt.setStartTime(submissionDTO.getStartTime());
        attempt.setEndTime(submissionDTO.getEndTime());
        attempt.setTimeSpentMinutes((int) timeSpent.toMinutes());
        attempt.setQuiz(quiz);
        attempt .setScore(score);
        attempt.setAnswerQuestions(questions);
        if(score >= Math.ceil(quiz.getNumberOfQuestions() / 2) )
            attempt.setStatus("PASSED");
        else
            attempt.setStatus("FAILED");
        StringBuilder feedBack = new StringBuilder("The Student has " + attempt.getStatus() + ". The Score is: " +
                score + " out of " + quiz.getNumberOfQuestions());
        attempt.setFeedback(feedBack.toString());

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

        QuizAttemptDTO savedAttemptDto = new QuizAttemptDTO();

        savedAttemptDto.setFeedback(savedAttempt.getFeedback());

        return savedAttemptDto;
    }

    @Override
    public List<Quiz> getQuizzesByCourse(Long courseId) {
        return quizRepository.findByCourseId(courseId);
    }


//    @Override
//    public Quiz updateQuiz(Long quizId, Quiz quizDetails) {
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id " + quizId));
//
//        quiz.setDuration(quizDetails.getDuration());
//        return quizRepository.save(quiz);
//    }

    @Override
    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }
    public Optional<Quiz> getQuizByCourseAndQuizId(Long courseId, Long quizId) {
        // نستخدم الدالة الجديدة في QuizRepository
        return quizRepository.findByCourseIdAndId(courseId, quizId);
    }

}
