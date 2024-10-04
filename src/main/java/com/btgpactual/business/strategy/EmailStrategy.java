package com.btgpactual.business.strategy;

import com.btgpactual.business.exceptions.EmailSendException;
import com.btgpactual.data.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailStrategy implements NotificationStrategy {

    private final JavaMailSender emailSender;

    @Value("${notification.email.from}")
    private String emailFrom;

    @Override
    public void sendNotification(User user, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailFrom);
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            emailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage(), e);
            throw new EmailSendException("Failed to send email", e);
        }
    }
}
