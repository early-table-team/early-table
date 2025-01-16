package com.gotcha.earlytable.domain.store.storeReservationType;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreReservationType;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreReservationTypeRepository extends JpaRepository<StoreReservationType, Long> {

    boolean existsByStoreStoreIdAndReservationType(Long storeId, ReservationType reservationType);

    void deleteByStoreReservationTypeIdAndReservationType(Long storeReservationTypeId, ReservationType reservationType);

    Optional<StoreReservationType> findByStoreStoreId(Long storeId);

    StoreReservationType findByStoreAndReservationType(Store store, @NotNull ReservationType reservationType);
}
