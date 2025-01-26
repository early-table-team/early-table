package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class StoreReviewResponseDto {

    private final Long storeId;

    private final Long reviewId;

    private final Integer rating;

    private final String reviewContents;

    private final Map<Integer, String> reviewImageUrlMap;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final String reviewerNickname;

    public StoreReviewResponseDto(Long storeId, Long reviewId, Integer rating, String reviewContents,
                                  Map<Integer, String> reviewImageUrlMap, LocalDateTime createdAt, LocalDateTime modifiedAt,
                                  String reviewerNickname) {
        this.storeId = storeId;
        this.reviewId = reviewId;
        this.rating = rating;
        this.reviewContents = reviewContents;
        this.reviewImageUrlMap = reviewImageUrlMap;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.reviewerNickname = reviewerNickname;
    }

    public static StoreReviewResponseDto toDto(Review review) {

        Map<Integer, String> imageFileUrlMap = new HashMap<>();
        for(FileDetail fileDetail : review.getFile().getFileDetailList()){
            imageFileUrlMap.put(fileDetail.getFileSeq(), fileDetail.getFileUrl());
        }

        return new StoreReviewResponseDto(
                review.getStore().getStoreId(),
                review.getReviewId(),
                review.getRating(),
                review.getReviewContent(),
                imageFileUrlMap,
                review.getCreatedAt(),
                review.getModifiedAt(),
                review.getUser().getNickName());
    }
}
