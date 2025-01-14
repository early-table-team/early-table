package com.gotcha.earlytable.domain.review.dto;

import com.gotcha.earlytable.domain.review.enums.ReviewTarget;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ReviewRequestDto {

    @NotNull(message = "평점을 입력하세요.")
    @Min(value = 1, message = "별점은 최소 1이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5이어야 합니다.")
    private final Integer rating;

    @NotBlank(message = "리뷰 내용을 입력하세요.")
    private final String reviewContent;

    private final List<MultipartFile> reviewImageList;

    @NotNull
    private final Long targetId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private final ReviewTarget targetObject;

    public ReviewRequestDto(Integer rating, String reviewContent, List<MultipartFile> reviewImageList, Long targetId, ReviewTarget targetObject) {
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.reviewImageList = reviewImageList;
        this.targetId = targetId;
        this.targetObject = targetObject;
    }
}
