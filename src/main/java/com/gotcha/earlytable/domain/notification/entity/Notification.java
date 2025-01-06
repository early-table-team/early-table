package com.gotcha.earlytable.domain.notification.entity;


import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provide_user", nullable = false)
    private User provideUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user", nullable = false)
    private User targetUser;

    private String content;

    private NotificationType type;

    private Boolean isRead;

    public Notification() {}
}
