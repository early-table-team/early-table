package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WaitingListResponseDto {
    Long storeId;
    Long waitingNumber;
    String storeName;
    WaitingStatus waitingStatus;
    LocalDateTime creatAt;

    public WaitingListResponseDto(Waiting waiting) {

        this.storeId = waiting.getStore().getStoreId();
        this.waitingNumber = waiting.getWaitingId();
        this.storeName = waiting.getStore().getStoreName();
        this.waitingStatus = waiting.getWaitingStatus();
        this.creatAt = waiting.getCreatedAt();

    }
}
