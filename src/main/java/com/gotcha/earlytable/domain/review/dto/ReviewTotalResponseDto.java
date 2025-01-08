package com.gotcha.earlytable.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewTotalResponseDto {
    private Integer ratingStat1;
    private Integer ratingStat2;
    private Integer ratingStat3;
    private Integer ratingStat4;
    private Integer ratingStat5;
    private Integer countTotal;
    private Double ratingAverage;

    public ReviewTotalResponseDto(Integer ratingStat1, Integer ratingStat2, Integer ratingStat3, Integer ratingStat4, Integer ratingStat5, Integer countTotal, Double ratingAverage) {
        this.ratingStat1 = ratingStat1;
        this.ratingStat2 = ratingStat2;
        this.ratingStat3 = ratingStat3;
        this.ratingStat4 = ratingStat4;
        this.ratingStat5 = ratingStat5;
        this.countTotal = countTotal;
        this.ratingAverage = ratingAverage;
    }
}