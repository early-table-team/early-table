package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.domain.notification.dto.TokenNotificationRequestDto;
import com.gotcha.earlytable.domain.notification.dto.TokenSaveRequestDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fcm")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/token/sendMessageTest")
    public ResponseEntity<Void> sendMessageToken(@RequestBody TokenNotificationRequestDto tokenNotificationRequestDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.sendByToken(tokenNotificationRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
