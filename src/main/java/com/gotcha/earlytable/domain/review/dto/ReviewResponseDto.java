package com.gotcha.earlytable.domain.review.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewResponseDto {

    private final Long storeId;

    private final Long reviewId;

    private final Integer rating;

    private final String reviewContents;

    private final Map<Integer, String> reviewImageUrlMap;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public ReviewResponseDto(Long storeId, Long reviewId, Integer rating, String reviewContents,
                             Map<Integer, String> reviewImageUrlMap, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.storeId = storeId;
        this.reviewId = reviewId;
        this.rating = rating;
        this.reviewContents = reviewContents;
        this.reviewImageUrlMap = reviewImageUrlMap;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ReviewResponseDto toDto(Review review) {

        Map<Integer, String> imageFileUrlMap = new HashMap<>();
        for(FileDetail fileDetail : review.getFile().getFileDetailList()){
            imageFileUrlMap.put(fileDetail.getFileSeq(), fileDetail.getFileUrl());
        }

            return new ReviewResponseDto(review.getStore().getStoreId(), review.getReviewId(),
                                         review.getRating(), review.getReviewContent(), imageFileUrlMap,
                                         review.getCreatedAt(), review.getModifiedAt());
    }
}
