package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.review.dto.ReviewRequestDto;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
public class ReviewController {

    private ReviewService reviewService;
    private ReviewRepository reviewRepository;

    @PostMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable Long storeId,
                                                          @ModelAttribute ReviewRequestDto reviewRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        ReviewResponseDto createReviewResponseDto = reviewService.createReview(storeId, user, reviewRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponseDto);

    }
}
