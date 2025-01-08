package com.gotcha.earlytable.domain.review.dto;

import com.gotcha.earlytable.domain.file.entity.ImageFile;
import com.gotcha.earlytable.domain.review.entity.Review;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewResponseDto {
    private final Integer rating;
    private final String reviewContent;
    private final Map<Integer, String> reviewImageUrlMap;

    public ReviewResponseDto(Integer rating, String reviewContent, Map<Integer, String> reviewImageUrlMap) {
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.reviewImageUrlMap = reviewImageUrlMap;
    }

    public static ReviewResponseDto toDto(Review review) {

        Map<Integer, String> imageFileUrlMap = new HashMap<>();
        for(ImageFile imageFile : review.getFile().getImageFileList()){
            imageFileUrlMap.put(imageFile.getFileSeq(), imageFile.getFileUniqueName());
        }

            return new ReviewResponseDto(
                    review.getRating(),
                    review.getReviewContent(),
                    imageFileUrlMap
            );
    }
}
