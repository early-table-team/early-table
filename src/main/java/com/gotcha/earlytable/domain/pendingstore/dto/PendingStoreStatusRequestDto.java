package com.gotcha.earlytable.domain.pendingstore.dto;

import com.gotcha.earlytable.domain.pendingstore.enums.PendingStoreStatus;
import lombok.Getter;

@Getter
public class PendingStoreStatusRequestDto {

    private final PendingStoreStatus pendingStoreStatus;

    public PendingStoreStatusRequestDto(PendingStoreStatus pendingStoreStatus) {
        this.pendingStoreStatus = pendingStoreStatus;
    }

}
