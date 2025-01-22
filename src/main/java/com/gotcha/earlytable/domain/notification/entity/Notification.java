package com.gotcha.earlytable.domain.notification.entity;


import com.gotcha.earlytable.global.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long targetUserId;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Boolean isRead;

    private String createdAt;

    public Notification() {}


    public Notification(Long targetUserId, String content, NotificationType type) {
        this.targetUserId = targetUserId;
        this.content = content;
        this.type = type;
        this.isRead = false;
        this.createdAt = LocalDateTime.now().toString();
    }

    public void read() {
        isRead = true;
    }
}
