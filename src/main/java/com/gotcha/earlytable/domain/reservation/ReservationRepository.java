package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default Reservation findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("select r from Reservation r join r.party p join p.partyPeople pp where pp.user = :user")
    Page<Reservation> findByUser(User user, Pageable pageable);


    int countByReservationDateAndReservationTimeAndTableSizeAndReservationStatusNot(LocalDate date, LocalTime reservationTime, int tableMaxNumber, ReservationStatus reservationStatus);
}
