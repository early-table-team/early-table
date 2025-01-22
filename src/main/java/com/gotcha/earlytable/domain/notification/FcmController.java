package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.domain.notification.dto.TokenNotificationRequestDto;
import com.gotcha.earlytable.domain.notification.dto.TokenSaveRequestDto;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fcm")
public class FcmController {
    private final FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    /**
     * 유저의 FCM 토큰 db 저장 API
     * /fcm/auth 페이지에서 알림 수락 -> FCM 토큰 + JWT 토큰이 DB에 저장됨
     */
    @PostMapping("/token")
    public ResponseEntity<Void> getToken(@Valid @RequestBody TokenSaveRequestDto tokenSaveRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        fcmService.saveToken(tokenSaveRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * FCM 알림 전송 API(Dto 이용)
     * @param tokenNotificationRequestDto
     * @param userDetails
     * @return
     */
    @PostMapping("/token/sendMessageTest")
    public ResponseEntity<Void> sendNotificationByTokenTest(@RequestBody TokenNotificationRequestDto tokenNotificationRequestDto,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        fcmService.sendNotificationByToken(tokenNotificationRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
