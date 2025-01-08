package com.gotcha.earlytable.domain.pendingstore.dto;

import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.pendingstore.enums.PendingStoreType;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PendingStoreResponseListDto {

    private final Long storeId;

    private final Long userId;

    private final String userName;

    private final String storeName;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final StoreStatus storeStatus;

    private final PendingStoreType pendingStoreType;

    public PendingStoreResponseListDto(Long storeId, Long userId, String userName, String storeName,
                                       LocalDateTime createdAt, LocalDateTime modifiedAt,
                                       StoreStatus storeStatus, PendingStoreType pendingStoreType) {
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.storeName = storeName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.storeStatus = storeStatus;
        this.pendingStoreType = pendingStoreType;
    }

    public static PendingStoreResponseListDto toDto(PendingStore pendingStore) {

        return new PendingStoreResponseListDto(pendingStore.getStoreId(), pendingStore.getUser().getId(),
                pendingStore.getUser().getNickName(), pendingStore.getStoreName(),
                pendingStore.getCreatedAt(), pendingStore.getModifiedAt(),
                pendingStore.getStoreStatus(), pendingStore.getPendingStoreType()
        );
    }

}
