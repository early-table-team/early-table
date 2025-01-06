package com.gotcha.earlytable.domain.Review;

import com.gotcha.earlytable.domain.Review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
