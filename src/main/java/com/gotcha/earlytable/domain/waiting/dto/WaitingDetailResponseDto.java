package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WaitingDetailResponseDto {
    Long waitingId;
    Integer waitingNumber;
    Integer personnelCount;
    String phoneNumber;
    WaitingStatus waitingStatus;
    LocalDateTime createdAt;

    public WaitingDetailResponseDto(Waiting waiting) {
        this.waitingId = waiting.getWaitingId();
        this.waitingNumber = waiting.getWaitingNumber();
        this.personnelCount = waiting.getPersonnelCount();
        this.phoneNumber = waiting.getPhone();
        this.waitingStatus = waiting.getWaitingStatus();
        this.createdAt = waiting.getCreatedAt();
    }
}
