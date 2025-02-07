package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
public class SseEmitterController {

    private final SseEmitterService sseEmitterService;

    public SseEmitterController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }

    /**
     * 클라이언트의 이벤트 구독을 수락한다. text/event-stream은 SSE를 위한 Mime Type이다. 서버 -> 클라이언트로 이벤트를 보낼 수 있게된다.
     * *참고로 produces는 반환하는 데이터 타입을 정의하고, consumes는 들어오는 데이터 타입을 정의한다.
     * (클라이언트에서 구독 요청을 할 때의 mediaType도 text/event-stream이다.)
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sseEmitterService.subscribe(userDetails.getUser().getId());
    }

}