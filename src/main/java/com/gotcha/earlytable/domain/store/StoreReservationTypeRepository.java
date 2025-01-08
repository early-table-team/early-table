package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreReservationType;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreReservationTypeRepository extends JpaRepository<StoreReservationType, Long> {


    StoreReservationType findByStore(Store store);

}
