package com.btgpactual.data.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private BigDecimal balance;
    private NotificationType notificationPreference;
    private String email;
    private String phoneNumber;

    public enum NotificationType {
        EMAIL,
        SMS
    }
}
