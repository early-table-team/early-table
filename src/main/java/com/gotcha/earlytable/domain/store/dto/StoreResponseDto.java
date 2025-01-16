package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class StoreResponseDto {

    private final Long storeId;

    private final String storeName;

    private final String storeTel;

    private final String storeContents;

    private final String storeAddress;

    private final StoreCategory storeCategory;

    private final String ownerName;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final StoreStatus storeStatus;

    private final Map<Integer, String> storeImageUrlMap;

    public StoreResponseDto(Long storeId, String storeName, String storeTel, String storeContents, String storeAddress,
                            StoreCategory storeCategory, String ownerName, LocalDateTime createdAt, LocalDateTime modifiedAt,
                            StoreStatus storeStatus, Map<Integer, String> storeImageUrlMap) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeContents = storeContents;
        this.storeAddress = storeAddress;
        this.storeCategory = storeCategory;
        this.ownerName = ownerName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.storeStatus = storeStatus;
        this.storeImageUrlMap = storeImageUrlMap;
    }


    public static StoreResponseDto toDto(Store store) {
        Map<Integer, String> imageFileUrlMap = new HashMap<>();

        for(FileDetail fileDetail : store.getFile().getFileDetailList()){
            imageFileUrlMap.put(fileDetail.getFileSeq(), fileDetail.getFileUrl());
        }

        return new StoreResponseDto(store.getStoreId(), store.getStoreName(), store.getStoreTel(),
                                    store.getStoreContents(), store.getStoreAddress(), store.getStoreCategory(),
                                    store.getUser().getNickName(), store.getCreatedAt(), store.getModifiedAt(),
                                    store.getStoreStatus(), imageFileUrlMap);
    }
}
