package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    default Waiting findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    int countByStoreAndCreatedAtBetween(Store store, LocalDateTime from, LocalDateTime to);

    int countByStoreAndWaitingStatusAndCreatedAtBetweenAndWaitingNumberLessThanEqual(Store store,
                                                                                     WaitingStatus waitingStatus,
                                                                                     LocalDateTime from,
                                                                                     LocalDateTime to,
                                                                                     Integer waitingNumber);

}
