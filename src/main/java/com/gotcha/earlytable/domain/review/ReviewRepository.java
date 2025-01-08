package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    default Review findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    // 선택 가게 리뷰들의 평균 별점
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store = :store")
    Double findAverageRatingByStore(Store store);

    Long countReviewsByStore(Store store);

}
