package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.review.dto.ReviewRequestDto;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewTotalResponseDto;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.review.enums.ReviewStatus;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final FileService fileService;
    private final StoreRepository storeRepository;

    public ReviewService(ReviewRepository reviewRepository, FileService fileService, StoreRepository storeRepository) {
        this.reviewRepository = reviewRepository;
        this.fileService = fileService;
        this.storeRepository = storeRepository;
    }

    /**
     * 리뷰 생성 서비스 메소드
     */
    @Transactional
    public ReviewResponseDto createReview(Long storeId, User user, ReviewRequestDto reviewRequestDto) throws IOException {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        File file = fileService.createFile();

        Review review = new Review(
                reviewRequestDto.getRating(),
                reviewRequestDto.getReviewContent(),
                ReviewStatus.NORMAL,
                store,
                user,
                file
        );

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.toDto(savedReview);
    }

    /**
     * 리뷰 수정 서비스 메서드
     */
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, User user, ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        //리뷰 수정
        review.updateReview(reviewRequestDto);

        //리뷰 저장
        reviewRepository.save(review);

        return ReviewResponseDto.toDto(review);
    }

    /**
     * 가게 전체 리뷰 조회 서비스 메서드
     */
    public List<ReviewResponseDto> getStoreReviews(Long storeId) {
        List<Review> reviews = reviewRepository.findAllByStoreStoreId(storeId);

        return reviews.stream().map(ReviewResponseDto::toDto).toList();
    }

    /**
     * 나의 전체 리뷰 조회 서비스 메서드
     */
    public List<ReviewResponseDto> getMyReviews(User user) {
        List<Review> reviews = reviewRepository.findAllByUserId(user.getId());

        return reviews.stream().map(ReviewResponseDto::toDto).toList();
    }

    /**
     * 가게 리뷰 평점 조회 서비스 메서드
     */
    public ReviewTotalResponseDto getStoreReviewTotal(Long storeId) {
        ReviewTotalResponseDto reviewTotalResponseDto = reviewRepository.findStatisticsByStoreId(storeId);

        return reviewTotalResponseDto;
    }

    /**
     * 리뷰 삭제 서비스 메서드
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        //탈퇴 상태로 업데이트
        review.updateReviewStatusToDeleted();
    }
}
