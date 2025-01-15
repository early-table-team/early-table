package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.user.dto.UserResponseDto;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import lombok.Getter;

@Getter
public class StoreSearchResponseDto {

    private static String storeName;

    private static String imageUrl;

    private static Double starPoint;

    private static Integer reviewCount;

    private static StoreCategory storeCategory;

    public StoreSearchResponseDto(Store store) {
        this.storeName = store.getStoreName();
        this.imageUrl = store.getFile().getFileDetailList().stream()
                .filter(fileDetail -> fileDetail.getFileStatus().equals(FileStatus.REPRESENTATIVE))
                .findFirst()
                .map(FileDetail::getFileUrl)
                .orElse(null);
        this.starPoint = store.getReviewList().stream().mapToDouble(Review::getRating).average().orElseThrow(()->new CustomException(ErrorCode.BAD_REQUEST));
        this.reviewCount = store.getReviewList().size();
        this.storeCategory = store.getStoreCategory();
    }

    public static StoreSearchResponseDto toDto(Store store) {
        return new StoreSearchResponseDto(store);
    }
}
