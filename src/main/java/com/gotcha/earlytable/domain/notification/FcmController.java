package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.domain.notification.dto.TokenSaveRequestDto;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fcm")
public class FcmController {
    private final NotificationService notificationService;

    public FcmController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * fcm 테스트 페이지 - 로그인 페이지(메인)
     * 로그인 후 /fcm/auth (getNotiAuth)로 리다이렉트
     */
    @GetMapping("/home")
    public String home() {
        return "login.html";
    }

    /**
     * fcm 테스트 페이지 - 알림 설정 페이지
     */
    @GetMapping("/auth")
    public String getNotiAuth() {
        return "fcm-notification.html";
    }

    /**
     * FCM 토큰 서버 저장 API
     * /fcm/auth 페이지에서 알림 수락 -> FCM 토큰 + JWT 토큰이 DB에 저장됨
     */
    @PostMapping("/token")
    public ResponseEntity<Void> getToken(@Valid @RequestBody TokenSaveRequestDto tokenSaveRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        notificationService.saveToken(tokenSaveRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
