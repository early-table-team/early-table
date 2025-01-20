package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.global.enums.NotificationType;
import lombok.Getter;

@Getter
public class NotificationMessage {
    private Long userId;
    private Object data;
    private NotificationType notificationType;

    public NotificationMessage(Long userId, Object data, NotificationType notificationType) {
        this.userId = userId;
        this.data = data;
        this.notificationType = notificationType;
    }

    public NotificationMessage() {
    }


}