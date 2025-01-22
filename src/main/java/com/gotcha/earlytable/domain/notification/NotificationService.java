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
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final FcmRepository fcmRepository;

    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository, FcmRepository fcmRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.fcmRepository = fcmRepository;
    }


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
            FcmToken fcmToken = fcmRepository.findByIdOrElseThrow(user.getId());
            fcmToken.update(tokenSaveRequestDto.getToken());
            fcmRepository.save(fcmToken);
        }
    }

    public void sendByToken(TokenNotificationRequestDto tokenNotificationRequestDto, User user) {
        String token = fcmRepository.findByUserId(user.getId()).getToken();

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
}
