package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.domain.notification.entity.FcmToken;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmRepository extends JpaRepository<FcmToken, Long> {

    default FcmToken findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    FcmToken findByUserId(Long id);

    boolean existsByUserId(Long id);
}
