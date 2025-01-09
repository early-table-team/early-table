package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.AvailableTable;
import com.gotcha.earlytable.domain.store.entity.ReservationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AvailableTableRepository extends JpaRepository<AvailableTable, Integer>, AvailableTableRepositoryQuery {

    AvailableTable findByReservationMaster(ReservationMaster reservationMaster);

}
