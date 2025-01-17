package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

@Getter
public class WaitingResponseDto {
    Long storeId;
    Long waitingId;
    Long waitingNumber;
    String storeName;
    WaitingType waitingType;
    WaitingStatus waitingStatus;


    public WaitingResponseDto(Waiting waiting) {

        this.storeId = waiting.getStore().getStoreId();
        this.waitingId = waiting.getWaitingId();
        this.waitingNumber = waiting.getWaitingId();
        this.storeName = waiting.getStore().getStoreName();
        this.waitingType = waiting.getWaitingType();
        this.waitingStatus = waiting.getWaitingStatus();

    }
}
