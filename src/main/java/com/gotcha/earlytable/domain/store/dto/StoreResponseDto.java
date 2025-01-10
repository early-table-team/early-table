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

    private final String storeAddress;

    private final StoreCategory storeCategory;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final StoreStatus storeStatus;

    private final Map<Integer, String> storeImageUrlMap;

    public StoreResponseDto(Long storeId, String storeName, String storeTel, String storeAddress,
                            StoreCategory storeCategory, LocalDateTime createdAt, LocalDateTime modifiedAt,
                            StoreStatus storeStatus, Map<Integer, String> storeImageUrlMap) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeAddress = storeAddress;
        this.storeCategory = storeCategory;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.storeStatus = storeStatus;
        this.storeImageUrlMap = storeImageUrlMap;
    }


    public static StoreResponseDto toDto(Store store) {
        Map<Integer, String> imageFileUrlMap = new HashMap<>();

        for(FileDetail fileDetail : store.getFile().getFileDetailList()){
            imageFileUrlMap.put(fileDetail.getFileSeq(), fileDetail.getFileUniqueName());
        }

        return new StoreResponseDto(store.getStoreId(),
                store.getStoreName(), store.getStoreTel(),
                store.getStoreAddress(), store.getStoreCategory(),
                store.getCreatedAt(), store.getModifiedAt(),
                store.getStoreStatus(), imageFileUrlMap
                );
    }
}
