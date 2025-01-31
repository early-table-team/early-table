package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.store.entity.Store;
import lombok.Getter;

@Getter
public class StoreSearchResponseDto {

    private Long storeId;

    private String storeName;

    private String storeContents;

    private String imageUrl;

    private Double starPoint;

    private  Integer reviewCount;

    private  String storeCategory;

    public StoreSearchResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storeContents = store.getStoreContents();
        this.imageUrl = store.getFile().getFileDetailList().stream()
                .filter(fileDetail -> fileDetail.getFileStatus().equals(FileStatus.REPRESENTATIVE))
                .findFirst()
                .map(FileDetail::getFileUrl)
                .orElse(null);
        this.starPoint = store.getReviewList().stream().mapToDouble(Review::getRating).average().orElse(0);
        this.reviewCount = store.getReviewList().size();
        this.storeCategory = store.getStoreCategory().getCategoryName();
    }

    public static StoreSearchResponseDto toDto(Store store) {
        return new StoreSearchResponseDto(store);
    }
}
