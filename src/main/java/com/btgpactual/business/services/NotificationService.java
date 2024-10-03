package com.btgpactual.business.services;

import com.btgpactual.business.exceptions.EmailSendException;
import com.btgpactual.data.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender emailSender;

    public void sendNotification(User user, String subject, String message) {
        if (user.getNotificationPreference() == User.NotificationType.EMAIL) {
            sendEmail(user.getEmail(), subject, message);
        } else {
            sendSMS(user.getPhoneNumber(), message);
        }
    }

    private void sendEmail(String email, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("noreply@btgpactual.com");
            mailMessage.setTo(email);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            emailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
             throw new EmailSendException("Failed to send email", e);
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        // Implementación del envío de SMS
        // Por ahora, solo imprimimos en consola
        System.out.println("SMS sent to " + phoneNumber + ": " + message);
    }
}
