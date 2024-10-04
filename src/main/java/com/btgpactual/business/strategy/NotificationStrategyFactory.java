package com.btgpactual.business.strategy;

import com.btgpactual.data.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationStrategyFactory {
    private final EmailStrategy emailStrategy;
    private final SMSStrategy smsStrategy;

    public NotificationStrategy getStrategy(User.NotificationType type) {
        return switch (type) {
            case EMAIL -> emailStrategy;
            case SMS -> smsStrategy;
            default -> throw new IllegalArgumentException("Unsupported notification type: " + type);
        };
    }
}
