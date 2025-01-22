package com.gotcha.earlytable.domain.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waiting")
public class WaitingSequenceController {

    private final WaitingSequenceService waitingSequenceService;

    public WaitingSequenceController(WaitingSequenceService waitingSequenceService) {
        this.waitingSequenceService = waitingSequenceService;
    }

    /**
     * 현재 내 순서 조회 API
     *
     * @param waitingId
     * @return 현재 내 순서
     */
    @GetMapping("/{waitingId}/now")
    public ResponseEntity<Long> getNowSeqNumber(@PathVariable Long waitingId) {

        long nowSeqNum = waitingSequenceService.getNowSeqNumber(waitingId);

        return ResponseEntity.ok(nowSeqNum);
    }
}
