package com.btgpactual.business.services;

import com.btgpactual.business.strategy.NotificationStrategy;
import com.btgpactual.business.strategy.NotificationStrategyFactory;
import com.btgpactual.data.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationStrategyFactory strategyFactory;

    public void sendNotification(User user, String subject, String message) {
        NotificationStrategy strategy = strategyFactory.getStrategy(user.getNotificationPreference());
        strategy.sendNotification(user, subject, message);
    }
}
