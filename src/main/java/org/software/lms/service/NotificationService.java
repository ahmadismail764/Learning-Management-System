package org.software.lms.service;

import org.software.lms.dto.NotificationDTO;
import org.software.lms.model.*;
import org.software.lms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Notification createNotification(Long userId, Long courseId, String title, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Notification notification = new Notification(user, course, title, message);
        return notificationRepository.save(notification);
    }

    public List<NotificationDTO> findByUserId(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> findByUserIdAndIsReadFalse(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setCourseTitle(notification.getCourse() != null ? notification.getCourse().getTitle() : null);
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setRead(notification.getRead());
        return dto;
    }
}