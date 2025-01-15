package com.gotcha.earlytable.domain.store.storeReservation;

import com.gotcha.earlytable.domain.store.entity.StoreReservationType;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreReservationTypeRepository extends JpaRepository<StoreReservationType, Long> {

    boolean existsByStoreStoreIdAndReservationType(Long storeId, ReservationType reservationType);

    void deleteByStoreReservationTypeIdAndReservationType(Long storeReservationTypeId, ReservationType reservationType);

    Optional<StoreReservationType> findByStoreStoreId(Long storeId);
}
