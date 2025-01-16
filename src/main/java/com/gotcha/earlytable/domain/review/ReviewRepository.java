package com.gotcha.earlytable.domain.review;

import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.review.enums.ReviewStatus;
import com.gotcha.earlytable.domain.review.enums.ReviewTarget;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    default Review findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    // 선택 가게 리뷰들의 평균 별점
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store = :store and r.reviewStatus = :reviewStatus")
    Double findAverageRatingByStore(Store store, ReviewStatus reviewStatus);

    Long countReviewsByStoreAndReviewStatus(Store store, ReviewStatus reviewStatus);


    // 선택 가게 리뷰들의 리뷰 별점 통계
    @Query("select sum(case when r.rating = 1 then 1 else 0 end) as ratingStat1," +
            "      sum(case when r.rating = 2 then 1 else 0 end) as ratingStat2," +
            "      sum(case when r.rating = 3 then 1 else 0 end) as ratingStat3," +
            "      sum(case when r.rating = 4 then 1 else 0 end) as ratingStat4," +
            "      sum(case when r.rating = 5 then 1 else 0 end) as ratingStat5," +
            "      count(r.rating) as countTotal," +
            "      round(avg(r.rating),1) as ratingAverage" +
            "  from Review r" +
            " where r.store.storeId = :storeId " +
            " and r.reviewStatus = :reviewStatus ")
    Map<String, Number> findStatisticsByStoreId(@Param("storeId") Long storeId, @Param("reviewStatus") ReviewStatus reviewStatus);


    boolean existsByTargetIdAndReviewTarget(Long targetId, ReviewTarget reviewTarget);

    List<Review> findAllByUserIdAndReviewStatus(Long id, ReviewStatus reviewStatus);

    List<Review> findAllByStoreStoreIdAndReviewStatus(Long storeId, ReviewStatus reviewStatus);

}
