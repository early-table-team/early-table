package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import lombok.Getter;

@Getter
public class WaitingResponseDto {
    private final Long storeId;
    private final Long waitingId;
    private final String storeName;
    private final String waitingType;
    private final String waitingStatus;
    private final String datetime;

    public WaitingResponseDto(Waiting waiting) {

        this.storeId = waiting.getStore().getStoreId();
        this.waitingId = waiting.getWaitingId();
        this.storeName = waiting.getStore().getStoreName();
        this.waitingType = waiting.getWaitingType().getValue();
        this.waitingStatus = waiting.getWaitingStatus().getValue();
        this.datetime = waiting.getCreatedAt().toLocalDate().toString() + " " + waiting.getCreatedAt().withNano(0).toLocalTime();
    }
}
