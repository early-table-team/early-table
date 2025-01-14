package com.gotcha.earlytable.domain.store.storeHour;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreHour;
import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreHourRepository extends JpaRepository<StoreHour, Long> {

    default StoreHour findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }
    StoreHour findByStoreAndDayOfWeek(Store store, DayOfWeek dayOfWeek);
    List<StoreHour> findByStoreAndDayStatus(Store store, DayStatus dayStatus);

}
