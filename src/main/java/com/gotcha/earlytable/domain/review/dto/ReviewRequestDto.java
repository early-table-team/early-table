package com.gotcha.earlytable.domain.review.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ReviewRequestDto {
    private final Integer rating;
    private final String reviewContent;
    private final List<MultipartFile> reviewImageList;

    public ReviewRequestDto(Integer rating, String reviewContent, List<MultipartFile> reviewImageList) {
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.reviewImageList = reviewImageList;
    }
}
