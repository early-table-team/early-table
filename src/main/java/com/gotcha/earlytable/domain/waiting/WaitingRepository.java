package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    default Waiting findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    List<Waiting> findByStoreAndWaitingTypeAndWaitingStatus(Store store, WaitingType waitingType, WaitingStatus waitingStatus);

    Long countByStoreAndCreatedAtBetweenAndWaitingType(Store store, LocalDateTime localDateTime, LocalDateTime localDateTime1, WaitingType waitingType);

    List<Waiting> findByStoreAndWaitingTypeAndWaitingStatusNotAndCreatedAtBetween(Store store, WaitingType waitingType, WaitingStatus waitingStatus, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
