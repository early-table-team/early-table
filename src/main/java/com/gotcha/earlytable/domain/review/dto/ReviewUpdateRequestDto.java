package com.gotcha.earlytable.domain.review.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ReviewUpdateRequestDto {
    private final Integer rating;
    private final String reviewContent;
    private final List<MultipartFile> newReviewImageList;
    private final List<String> fileUrlList;

    public ReviewUpdateRequestDto(Integer rating, String reviewContent,
                                  List<MultipartFile> newReviewImageList, List<String> fileUrlList) {
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.newReviewImageList = newReviewImageList;
        this.fileUrlList = fileUrlList;
    }
}
