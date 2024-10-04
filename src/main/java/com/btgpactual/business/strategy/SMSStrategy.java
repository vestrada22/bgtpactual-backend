package com.btgpactual.business.strategy;

import com.btgpactual.config.TwilioConfig;
import com.btgpactual.business.exceptions.SMSSendException;
import com.btgpactual.data.entities.User;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SMSStrategy implements NotificationStrategy {

    private final TwilioConfig twilioConfig;

    @Override
    public void sendNotification(User user, String subject, String message) {
        try {
            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
            Message.creator(
                    new PhoneNumber(user.getPhoneNumber()),
                    new PhoneNumber(twilioConfig.getPhoneNumber()),
                    message
            ).create();
        } catch (Exception e) {
            log.error("Failed to send SMS: {}", e.getMessage(), e);
            throw new SMSSendException("Failed to send SMS", e);
        }
    }
}
