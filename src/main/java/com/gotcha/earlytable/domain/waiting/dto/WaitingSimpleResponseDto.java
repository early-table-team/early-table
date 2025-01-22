package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import lombok.Getter;


@Getter
public class WaitingSimpleResponseDto {
    Long waitingId;
    Long waitingNumber;
    Integer personnelCount;
    String phoneNumber;

    public WaitingSimpleResponseDto(Waiting waiting) {
        this.waitingId = waiting.getWaitingId();
        this.waitingNumber = waiting.getWaitingNumber();
        this.personnelCount = waiting.getPersonnelCount();
        this.phoneNumber = waiting.getPhone();
    }
}
