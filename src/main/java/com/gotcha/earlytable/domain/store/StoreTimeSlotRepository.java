package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.StoreTimeSlot;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface StoreTimeSlotRepository extends JpaRepository<StoreTimeSlot, Long> {

    default StoreTimeSlot findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsByStoreStoreIdAndReservationTime(Long storeId, LocalTime reservationTime);

    List<StoreTimeSlot> findByStoreStoreId(Long storeId);


}
