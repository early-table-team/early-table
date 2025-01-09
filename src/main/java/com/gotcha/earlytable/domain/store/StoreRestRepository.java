package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.StoreRest;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StoreRestRepository extends JpaRepository<StoreRest, Long> {

    default StoreRest findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    List<StoreRest> findAllByStoreStoreIdAndStoreOffDayBetween(Long storeId, LocalDate startDate, LocalDate endDate);
}
