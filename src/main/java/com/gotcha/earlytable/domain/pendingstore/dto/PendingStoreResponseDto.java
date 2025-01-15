package com.gotcha.earlytable.domain.pendingstore.dto;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.pendingstore.enums.PendingStoreType;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class PendingStoreResponseDto {

    private final Long pendingStoreId;

    private final Long storeId;

    private final Long userId;

    private final String userName;

    private final String storeName;

    private final String storeTel;

    private final String storeAddress;

    private final StoreCategory storeCategory;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final StoreStatus storeStatus;

    private final Map<Integer, String> storeImageUrlMap;

    private final PendingStoreType pendingStoreType;

    public PendingStoreResponseDto(Long pendingStoreId,Long storeId, Long userId, String userName, String storeName, String storeTel,
                                   String storeAddress, StoreCategory storeCategory,
                                   LocalDateTime createdAt, LocalDateTime modifiedAt,
                                   StoreStatus storeStatus, PendingStoreType pendingStoreType,
                                   Map<Integer, String> storeImageUrlMap) {
        this.pendingStoreId = pendingStoreId;
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeAddress = storeAddress;
        this.storeCategory = storeCategory;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.storeStatus = storeStatus;
        this.pendingStoreType = pendingStoreType;
        this.storeImageUrlMap = storeImageUrlMap;
    }

    public static PendingStoreResponseDto toDto(PendingStore pendingStore, File file) {
        Map<Integer, String> imageFileUrlMap = new HashMap<>();

        for (FileDetail fileDetail : file.getFileDetailList()) {
            imageFileUrlMap.put(fileDetail.getFileSeq(), fileDetail.getFileUrl());
        }

        return new PendingStoreResponseDto(pendingStore.getPendingStoreId(),pendingStore.getStoreId(),
                pendingStore.getUser().getId(), pendingStore.getUser().getNickName(),
                pendingStore.getStoreName(), pendingStore.getStoreTel(),
                pendingStore.getStoreAddress(), pendingStore.getStoreCategory(),
                pendingStore.getCreatedAt(), pendingStore.getModifiedAt(),
                pendingStore.getStoreStatus(), pendingStore.getPendingStoreType(),
                imageFileUrlMap);
    }

}