package com.gotcha.earlytable.domain.notification.entity;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "fcm_token")
public class FcmToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String token;

//    private String device_type; //디바이스 타입(예: Android, iOS, Web, ..)
//
//    private String platform; //플랫폼 정보(예: Firebase, Web Push, ..)
//
//    private boolean isActive;

    public void setFCMToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public void update(String token) {
        this.token = token;
    }
}
