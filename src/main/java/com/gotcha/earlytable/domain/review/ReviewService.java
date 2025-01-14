package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.review.dto.ReviewRequestDto;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewTotalResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewUpdateRequestDto;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.review.enums.ReviewStatus;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final FileDetailService fileDetailService;

    public ReviewService(ReviewRepository reviewRepository, StoreRepository storeRepository, FileService fileService, FileDetailService fileDetailService) {
        this.reviewRepository = reviewRepository;
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.fileDetailService = fileDetailService;
    }


    /**
     * 리뷰 생성 서비스 메소드
     */
    @Transactional
    public ReviewResponseDto createReview(Long storeId, User user, ReviewRequestDto reviewRequestDto){

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        File file = fileService.createFile();

        // 이미지 파일들 저장
        fileDetailService.createImageFiles(reviewRequestDto.getReviewImageList(), file);

        Review review = new Review(
                reviewRequestDto.getRating(),
                reviewRequestDto.getReviewContent(),
                ReviewStatus.NORMAL,
                store, user, file
        );

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.toDto(savedReview);
    }

    /**
     * 리뷰 수정 서비스 메서드
     */
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) {

        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        //리뷰 수정 및 저장
        review.updateReview(reviewUpdateRequestDto);
        Review updatedReview = reviewRepository.save(review);

        // 이미지 수정
        fileDetailService.updateFileDetail(reviewUpdateRequestDto.getNewReviewImageList(), reviewUpdateRequestDto.getFileUrlList(), review.getFile());

        return ReviewResponseDto.toDto(updatedReview);
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

        Map<String, Object> result = reviewRepository.findStatisticsByStoreId(storeId);

        return new ReviewTotalResponseDto(
                ((Long) result.get("ratingStat1")).intValue(),
                ((Long) result.get("ratingStat2")).intValue(),
                ((Long) result.get("ratingStat3")).intValue(),
                ((Long) result.get("ratingStat4")).intValue(),
                ((Long) result.get("ratingStat5")).intValue(),
                ((Long) result.get("countTotal")).intValue(),
                (Double) result.get("ratingAverage")
        );
    }

    /**
     * 리뷰 삭제 서비스 메서드
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {

        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        // 리뷰가 내가 작성한게 맞나 확인하기
        if(!review.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        //탈퇴 상태로 업데이트
        review.updateReviewStatusToDeleted();
    }
}
