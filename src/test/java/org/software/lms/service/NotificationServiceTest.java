package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.software.lms.dto.NotificationDTO;
import org.software.lms.model.*;
import org.software.lms.repository.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNotification() {
        // Arrange
        Long userId = 1L;
        Long courseId = 1L;
        String title = "Test Notification";
        String message = "This is a test notification";

        User user = new User();
        user.setId(userId);

        Course course = new Course();
        course.setId(courseId);

        Notification notification = new Notification(user, course, title, message);
        notification.setId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        Notification result = notificationService.createNotification(userId, courseId, title, message);

        // Assert
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(message, result.getMessage());
        verify(userRepository).findById(userId);
        verify(courseRepository).findById(courseId);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testFindByUserId() {
        // Arrange
        Long userId = 1L;

        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setTitle("Notification 1");
        notification1.setMessage("Message 1");
        notification1.setCreatedAt(LocalDateTime.now());
        notification1.setRead(false);

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setTitle("Notification 2");
        notification2.setMessage("Message 2");
        notification2.setCreatedAt(LocalDateTime.now());
        notification2.setRead(true);

        when(notificationRepository.findByUserId(userId)).thenReturn(Arrays.asList(notification1, notification2));

        // Act
        var notifications = notificationService.findByUserId(userId);

        // Assert
        assertNotNull(notifications); // Ensure notifications list is not null
        assertEquals(2, notifications.size()); // Ensure two notifications are returned

        // Validate the first notification DTO
        NotificationDTO dto1 = notifications.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Notification 1", dto1.getTitle());
        assertEquals("Message 1", dto1.getMessage());

        // Validate the second notification DTO
        NotificationDTO dto2 = notifications.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Notification 2", dto2.getTitle());
        assertEquals("Message 2", dto2.getMessage());

        // Verify the repository interaction
        verify(notificationRepository).findByUserId(userId);
    }


    @Test
    void testFindByUserIdAndIsReadFalse() {
        // Arrange
        Long userId = 1L;

        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setTitle("Unread Notification 1");
        notification1.setMessage("Message 1");
        notification1.setRead(false);

        when(notificationRepository.findByUserIdAndIsReadFalse(userId)).thenReturn(Arrays.asList(notification1));

        // Act
        var unreadNotifications = notificationService.findByUserIdAndIsReadFalse(userId);

        // Assert
        assertNotNull(unreadNotifications);
        assertEquals(1, unreadNotifications.size());
        assertEquals("Unread Notification 1", unreadNotifications.get(0).getTitle());
        verify(notificationRepository).findByUserIdAndIsReadFalse(userId);
    }

    @Test
    void testMarkAsRead() {
        // Arrange
        Long notificationId = 1L;

        // Act
        notificationService.markAsRead(notificationId);

        // Assert
        verify(notificationRepository).markAsRead(notificationId);
    }
}
