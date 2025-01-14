package com.gotcha.earlytable.domain.review.entity;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.review.dto.ReviewUpdateRequestDto;
import com.gotcha.earlytable.domain.review.enums.ReviewStatus;
import com.gotcha.earlytable.domain.review.enums.ReviewTarget;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private Integer rating;

    private String reviewContent;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewTarget reviewTarget;

    private Long targetId;

    public Review(int rating, String reviewContent, ReviewStatus reviewStatus, Store store, User user, File file,
                  ReviewTarget reviewTarget, Long targetId) {
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.reviewStatus = reviewStatus;
        this.store = store;
        this.user = user;
        this.file = file;
        this.reviewTarget = reviewTarget;
        this.targetId = targetId;
    }

    public Review() {}

    public void updateReview(ReviewUpdateRequestDto reviewUpdateRequestDto) {
        if(reviewUpdateRequestDto.getRating() != null) {
            this.rating = reviewUpdateRequestDto.getRating();
        }
        if(reviewUpdateRequestDto.getReviewContent() != null) {
            this.reviewContent = reviewUpdateRequestDto.getReviewContent();
        }
    }

    public void updateReviewStatusToDeleted() {
        this.reviewStatus = ReviewStatus.DELETE;
    }
}
