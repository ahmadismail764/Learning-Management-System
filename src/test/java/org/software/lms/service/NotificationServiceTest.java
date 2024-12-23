//package org.software.lms.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.software.lms.model.Notification;
//import org.software.lms.repository.NotificationRepository;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class NotificationServiceTest {
//
//    @Mock
//    private NotificationRepository notificationRepository;
//
//    @InjectMocks
//    private NotificationService notificationService;
//
//    private Notification notification;
//
//    @BeforeEach
//    void setUp() {
//        notification = new Notification();
//        notification.setId(1L);
//        notification.setTitle("Test Notification");
//        notification.setMessage("This is a test message.");
//        notification.setUserId(1L);
//        notification.setIsRead(false);
//    }
//
//    @Test
//    void testCreateNotification() {
//        when(notificationRepository.save(notification)).thenReturn(notification);
//
//        Notification createdNotification = notificationService.createNotification("Test Notification", "This is a test message.", 1L);
//
//        assertNotNull(createdNotification);
//        assertEquals("Test Notification", createdNotification.getTitle());
//        assertEquals("This is a test message.", createdNotification.getMessage());
//        assertEquals(1L, createdNotification.getUserId());
//        verify(notificationRepository, times(1)).save(notification);
//    }
//
//    @Test
//    void testGetUnreadNotifications() {
//        Notification unreadNotification = new Notification();
//        unreadNotification.setId(2L);
//        unreadNotification.setTitle("Unread Notification");
//        unreadNotification.setMessage("Unread message.");
//        unreadNotification.setUserId(1L);
//        unreadNotification.setIsRead(false);
//
//        when(notificationRepository.findByUserIdAndUnread(1L)).thenReturn(Arrays.asList(unreadNotification));
//
//        List<Notification> unreadNotifications = notificationService.getUnreadNotifications(1L);
//
//        assertNotNull(unreadNotifications);
//        assertEquals(1, unreadNotifications.size());
//        assertEquals("Unread Notification", unreadNotifications.get(0).getTitle());
//        verify(notificationRepository, times(1)).findByUserIdAndUnread(1L);
//    }
//
//    @Test
//    void testMarkAsRead() {
//        notification.setIsRead(false);
//        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
//        when(notificationRepository.save(notification)).thenReturn(notification);
//
//        notificationService.markAsRead(1L);
//
//        assertTrue(notification.getIsRead());
//        verify(notificationRepository, times(1)).findById(1L);
//        verify(notificationRepository, times(1)).save(notification);
//    }
//
//    @Test
//    void testGetAllNotifications() {
//        Notification notification1 = new Notification();
//        notification1.setId(1L);
//        notification1.setTitle("Notification 1");
//        notification1.setMessage("Message 1");
//        notification1.setUserId(1L);
//        notification1.setIsRead(true);
//
//        Notification notification2 = new Notification();
//        notification2.setId(2L);
//        notification2.setTitle("Notification 2");
//        notification2.setMessage("Message 2");
//        notification2.setUserId(1L);
//        notification2.setIsRead(false);
//
//        when(notificationRepository.findByUserId(1L)).thenReturn(Arrays.asList(notification1, notification2));
//
//        List<Notification> allNotifications = notificationService.getAllNotifications(1L);
//
//        assertNotNull(allNotifications);
//        assertEquals(2, allNotifications.size());
//        assertEquals("Notification 1", allNotifications.get(0).getTitle());
//        assertEquals("Notification 2", allNotifications.get(1).getTitle());
//        verify(notificationRepository, times(1)).findByUserId(1L);
//    }
//
//    @Test
//    void testNotificationNotFoundForMarkAsRead() {
//        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(1L));
//    }
//}
