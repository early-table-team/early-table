package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
