package com.gotcha.earlytable.domain.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.gotcha.earlytable.domain.notification.dto.TokenNotificationRequestDto;
import com.gotcha.earlytable.domain.notification.dto.TokenSaveRequestDto;
import com.gotcha.earlytable.domain.notification.entity.FcmToken;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FcmService {

    private final UserRepository userRepository;
    private final FcmRepository fcmRepository;

    public FcmService(UserRepository userRepository, NotificationRepository notificationRepository, NotificationRepository notificationRepository1, FcmRepository fcmRepository) {
        this.userRepository = userRepository;
        this.fcmRepository = fcmRepository;
    }

    /**
     * 유저의 FCM 토큰 db에 저장하는 서비스 메서드
     */
    @Transactional
    public void saveToken(@Valid TokenSaveRequestDto tokenSaveRequestDto, User user) {
        //유저의 token 데이터 없으면 save, 존재하면 update
        if(!fcmRepository.existsByUserId(user.getId())) {
            //save
            FcmToken fcmToken = new FcmToken();
            fcmToken.setFCMToken(user, tokenSaveRequestDto.getToken());
            fcmRepository.save(fcmToken);
        } else {
            //update
            FcmToken fcmToken = fcmRepository.findByUserId(user.getId());
            fcmToken.update(tokenSaveRequestDto.getToken());
            fcmRepository.save(fcmToken);
        }
    }

    /**
     * FCM 알림 전송 서비스 메서드(Dto이용)
     */
    public void sendNotificationByToken(TokenNotificationRequestDto tokenNotificationRequestDto, User receivedUser) {
        String token = fcmRepository.findByUserId(receivedUser.getId()).getToken();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(tokenNotificationRequestDto.getTitle())
                        .setBody(tokenNotificationRequestDto.getContent())
                        .setImage(tokenNotificationRequestDto.getImg())
                        .build())
                //.putData("click_action", tokenNotificationRequestDto.getUrl())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * FCM 알림 전송 서비스 메서드
     */
    public void sendNotificationByToken(String title, String content, String imgUrl, User receivedUser) {
        String token = fcmRepository.findByUserId(receivedUser.getId()).getToken();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(content)
                        .setImage(imgUrl)
                        .build())
                //.putData("click_action", tokenNotificationRequestDto.getUrl())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
