package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.store.entity.ReservationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface ReservationMasterRepository extends JpaRepository<ReservationMaster, Long> {

    ReservationMaster findByTableMaxNumberAndReservationTime(Integer personnelCount, LocalTime localTime);

}
