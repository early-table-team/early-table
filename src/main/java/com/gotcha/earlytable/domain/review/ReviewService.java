package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.review.dto.ReviewRequestDto;
import com.gotcha.earlytable.domain.review.dto.ReviewResponseDto;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
                store,
                user,
                file
        );

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.toDto(savedReview);
    }
}
