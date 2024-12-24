package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.software.lms.model.Notification;
import org.software.lms.repository.NotificationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test message.");
        notification.setRead(false);
    }

    @Test
    void testCreateNotification() {
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification createdNotification = notificationService.createNotification(1L, 1L, "Test Notification", "This is a test message.");

        assertNotNull(createdNotification);
        assertEquals("Test Notification", createdNotification.getTitle());
        assertEquals("This is a test message.", createdNotification.getMessage());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testGetUnreadNotifications() {
        Notification unreadNotification = new Notification();
        unreadNotification.setId(2L);
        unreadNotification.setTitle("Unread Notification");
        unreadNotification.setMessage("Unread message.");
        unreadNotification.setRead(false);

        when(notificationRepository.findByUserIdAndIsReadFalse(1L)).thenReturn(Arrays.asList(unreadNotification));

        verify(notificationRepository, times(1)).findByUserIdAndIsReadFalse(1L);
    }

    @Test
    void testMarkAsRead() {
        notification.setRead(false);
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        notificationService.markAsRead(1L);

        assertTrue(notification.getRead());
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testGetAllNotifications() {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setTitle("Notification 1");
        notification1.setMessage("Message 1");
        notification1.setRead(true);

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setTitle("Notification 2");
        notification2.setMessage("Message 2");
        notification2.setRead(false);

        when(notificationRepository.findByUserId(1L)).thenReturn(Arrays.asList(notification1, notification2));

        verify(notificationRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testNotificationNotFoundForMarkAsRead() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(1L));
    }
}
