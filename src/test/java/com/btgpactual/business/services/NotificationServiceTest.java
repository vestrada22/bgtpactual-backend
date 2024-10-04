package com.btgpactual.business.services;

import static org.junit.jupiter.api.Assertions.*;

import com.btgpactual.business.strategy.NotificationStrategy;
import com.btgpactual.business.strategy.NotificationStrategyFactory;
import com.btgpactual.data.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationStrategyFactory strategyFactory;

    @Mock
    private NotificationStrategy notificationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenUserWithEmailPreference_whenSendNotification_thenUseEmailStrategy() {
        // Given
        User user = new User();
        user.setNotificationPreference(User.NotificationType.EMAIL);
        String subject = "Test Subject";
        String message = "Test Message";

        when(strategyFactory.getStrategy(User.NotificationType.EMAIL)).thenReturn(notificationStrategy);

        // When
        notificationService.sendNotification(user, subject, message);

        // Then
        verify(strategyFactory).getStrategy(User.NotificationType.EMAIL);
        verify(notificationStrategy).sendNotification(user, subject, message);
    }

    @Test
    void givenUserWithSMSPreference_whenSendNotification_thenUseSMSStrategy() {
        // Given
        User user = new User();
        user.setNotificationPreference(User.NotificationType.SMS);
        String subject = "Test Subject";
        String message = "Test Message";

        when(strategyFactory.getStrategy(User.NotificationType.SMS)).thenReturn(notificationStrategy);

        // When
        notificationService.sendNotification(user, subject, message);

        // Then
        verify(strategyFactory).getStrategy(User.NotificationType.SMS);
        verify(notificationStrategy).sendNotification(user, subject, message);
    }

    @Test
    void givenNullUser_whenSendNotification_thenThrowException() {
        // Given
        User user = null;
        String subject = "Test Subject";
        String message = "Test Message";

        // When & Then
        assertThrows(NullPointerException.class, () ->
                notificationService.sendNotification(user, subject, message));
    }

    @Test
    void givenUserWithNullPreference_whenSendNotification_thenThrowException() {
        // Given
        User user = new User();
        user.setNotificationPreference(null);
        String subject = "Test Subject";
        String message = "Test Message";

        // When & Then
        assertThrows(NullPointerException.class, () ->
                notificationService.sendNotification(user, subject, message));
    }
}