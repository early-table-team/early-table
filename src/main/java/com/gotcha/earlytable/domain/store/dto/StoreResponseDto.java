package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreReservationType;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class StoreResponseDto {

    private final Long storeId;

    private final String storeName;

    private final String storeTel;

    private final String storeContents;

    private final String storeAddress;

    private final String storeCategory;

    private final String ownerName;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final StoreStatus storeStatus;

    private final Map<Integer, String> storeImageUrlMap;

    private final List<ReservationType> storeTypeList = new ArrayList<>();

    private final List<WaitingType> waitingTypeList = new ArrayList<>();


    private final Double starPoint;

    private final Integer reviewCount;

    public StoreResponseDto(Long storeId, String storeName, String storeTel, String storeContents, String storeAddress,
                            StoreCategory storeCategory, String ownerName, LocalDateTime createdAt, LocalDateTime modifiedAt,
                            StoreStatus storeStatus, Map<Integer, String> storeImageUrlMap,
                            List<WaitingType> waitingTypeList , List<ReservationType> storeTypeList,
                            Double starPoint, Integer reviewCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeContents = storeContents;
        this.storeAddress = storeAddress;
        this.storeCategory = storeCategory.getCategoryName();
        this.ownerName = ownerName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.storeStatus = storeStatus;
        this.storeImageUrlMap = storeImageUrlMap;
        this.starPoint = starPoint;
        this.reviewCount = reviewCount;
        this.waitingTypeList.addAll(waitingTypeList);
        this.storeTypeList.addAll(storeTypeList);
    }


    public static StoreResponseDto toDto(Store store) {
        Map<Integer, String> imageFileUrlMap = new HashMap<>();

        for(FileDetail fileDetail : store.getFile().getFileDetailList()){
            imageFileUrlMap.put(fileDetail.getFileSeq(), fileDetail.getFileUrl());
        }

        List<WaitingType> waitingTypeList = new ArrayList<>();

        boolean isToGo = store.getStoreReservationTypeList().stream()
                .filter(srt -> srt.getReservationType().equals(ReservationType.REMOTE))
                .anyMatch(StoreReservationType::isToGo);

        if(isToGo){
            waitingTypeList.add(WaitingType.TO_GO);
        }

        boolean isDineIn = store.getStoreReservationTypeList().stream()
                .filter(srt -> srt.getReservationType().equals(ReservationType.REMOTE))
                .anyMatch(StoreReservationType::isDineIn);

        if(isDineIn){
            waitingTypeList.add(WaitingType.DINE_IN);
        }

        List<ReservationType> storeTypeList =
                store.getStoreReservationTypeList().stream().map(StoreReservationType::getReservationType).toList();

        Double starPoint = store.getReviewList().stream().mapToDouble(Review::getRating).average().orElse(0);

        return new StoreResponseDto(store.getStoreId(), store.getStoreName(), store.getStoreTel(),
                                    store.getStoreContents(), store.getStoreAddress(), store.getStoreCategory(),
                                    store.getUser().getNickName(), store.getCreatedAt(), store.getModifiedAt(),
                                    store.getStoreStatus(), imageFileUrlMap, waitingTypeList, storeTypeList,
                                    starPoint, store.getReviewList().size());
    }
}
