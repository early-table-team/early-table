package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreHour;
import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreHourRepository extends JpaRepository<StoreHour, Long> {

    StoreHour findByStoreAndDayOfWeek(Store store, DayOfWeek dayOfWeek);

}
