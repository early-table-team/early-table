package com.gotcha.earlytable.domain.waitingsetting;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingSettingRepository extends JpaRepository<WaitingSetting, Long> {

    default WaitingSetting findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsByWaitingSettingIdAndStoreUserId(Long waitingSettingId, Long userId);

    WaitingSetting findByStore(Store store);

    List<WaitingSetting> findAllByWaitingSettingStatus(WaitingSettingStatus waitingSettingStatus);
}
