package com.btgpactual.business.strategy;

import com.btgpactual.data.entities.User;

public interface NotificationStrategy {
    void sendNotification(User user, String subject, String message);
}
