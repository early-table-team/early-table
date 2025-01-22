package com.gotcha.earlytable.domain.notification;

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
}
