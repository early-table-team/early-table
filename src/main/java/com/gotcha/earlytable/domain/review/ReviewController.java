package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.review.dto.ReviewRequestDto;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewTotalResponseDto;
import com.gotcha.earlytable.domain.review.dto.ReviewUpdateRequestDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 리뷰 등록 API
     *
     * @param storeId
     * @param reviewRequestDto
     * @param userDetails
     * @return ReviewResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable Long storeId,
                                                          @Valid @ModelAttribute ReviewRequestDto reviewRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        ReviewResponseDto createReviewResponseDto = reviewService.createReview(storeId, user, reviewRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponseDto);
    }

    /**
     * 리뷰 수정 API
     *
     * @param reviewId
     * @param requestDto
     * @param userDetails
     * @return ReviewResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long reviewId,
                                                          @Valid @ModelAttribute ReviewUpdateRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        ReviewResponseDto updateReviewResponseDto = reviewService.updateReview(reviewId, requestDto, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(updateReviewResponseDto);
    }

    /**
     * 가게 전체 리뷰 조회 API
     *
     * @param storeId
     * @return List<ReviewResponseDto>
     */
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getStoreReviews(@PathVariable Long storeId) {
        List<ReviewResponseDto> storeReviewsResponseDtoList = reviewService.getStoreReviews(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(storeReviewsResponseDtoList);
    }

    /**
     * 나의 전체 리뷰 조회 API
     * @param userDetails
     * @return List<ReviewResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        List<ReviewResponseDto> myReviewsResponseDtoList = reviewService.getMyReviews(user);

        return ResponseEntity.status(HttpStatus.OK).body(myReviewsResponseDtoList);
    }

    /**
     * 가게 리뷰 평점 조회 API
     * @param storeId
     * @return ReviewTotalResponseDto
     */
    @GetMapping("/stores/{storeId}/reviews/total")
    public ResponseEntity<ReviewTotalResponseDto> getStoreReviewTotal(@PathVariable Long storeId) {
        ReviewTotalResponseDto reviewTotalResponseDto = reviewService.getStoreReviewTotal(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(reviewTotalResponseDto);
    }

    /**
     * 리뷰 삭제 API
     * @param reviewId
     * @param userDetails
     * @return String
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제가 완료되었습니다.");
    }
}
