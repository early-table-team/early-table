package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.review.dto.ReviewRequestDto;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewTotalResponseDto;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class ReviewController {

    private ReviewService reviewService;
    private ReviewRepository reviewRepository;

    /**
     * 리뷰 등록 API
     * @param storeId
     * @param reviewRequestDto
     * @param userDetails
     * @return ReviewResponseDto
     * @throws IOException
     */
    @PostMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable Long storeId,
                                                          @ModelAttribute ReviewRequestDto reviewRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        ReviewResponseDto createReviewResponseDto = reviewService.createReview(storeId, user, reviewRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponseDto);
    }

    /**
     * 리뷰 수정 API
     * @param reviewId
     * @param reviewRequestDto
     * @param userDetails
     * @return ReviewResponseDto
     * @throws IOException
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long reviewId,
                                                          @ModelAttribute ReviewRequestDto reviewRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        ReviewResponseDto updateReviewResponseDto = reviewService.updateReview(reviewId, user, reviewRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateReviewResponseDto);
    }

    /**
     * 가게 전체 리뷰 조회 API
     * @param storeId
     * @param reviewRequestDto
     * @return List<ReviewResponseDto>
     */
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getStoreReviews(@PathVariable Long storeId,
                                                             @ModelAttribute ReviewRequestDto reviewRequestDto) {
        List<ReviewResponseDto> storeReviewsResponseDtoList = reviewService.getStoreReviews(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(storeReviewsResponseDtoList);
    }

    /**
     * 나의 전체 리뷰 조회 API
     * @param reviewRequestDto
     * @param userDetails
     * @return List<ReviewResponseDto>
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(@ModelAttribute ReviewRequestDto reviewRequestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        List<ReviewResponseDto> myReviewsResponseDtoList = reviewService.getMyReviews(user);

        return ResponseEntity.status(HttpStatus.OK).body(myReviewsResponseDtoList);
    }

    /**
     * 가게 리뷰 평점 조회 API
     * @param storeId
     * @param reviewRequestDto
     * @return ReviewResponseDto
     */
    @GetMapping("/stores/{storeId}/reviews/total")
    public ResponseEntity<ReviewTotalResponseDto> getStoreReviewTotal(@PathVariable Long storeId,
                                                                      @ModelAttribute ReviewRequestDto reviewRequestDto) {
        ReviewTotalResponseDto reviewTotalResponseDto = reviewService.getStoreReviewTotal(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(reviewTotalResponseDto);
    }

    /**
     * 리뷰 삭제 API
     * @param reviewId
     * @param reviewRequestDto
     * @param userDetails
     * @return String
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId,
                                                          @ModelAttribute ReviewRequestDto reviewRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId);

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제가 완료되었습니다.");
    }
}
