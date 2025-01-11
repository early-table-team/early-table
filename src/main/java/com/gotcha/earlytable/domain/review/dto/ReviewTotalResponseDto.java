package com.gotcha.earlytable.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewTotalResponseDto {
    private final Integer ratingStat1;
    private final Integer ratingStat2;
    private final Integer ratingStat3;
    private final Integer ratingStat4;
    private final Integer ratingStat5;
    private final Integer countTotal;
    private final Double ratingAverage;

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