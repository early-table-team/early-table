package com.gotcha.earlytable.domain.reservation.entity;


import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private int personnelCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /*
    예약 초대 아이디 부분 추가
    */
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    public Reservation() {}

    public Reservation(LocalDate reservationDate, int personnelCount, Store store) {
        this.reservationDate = reservationDate;
        this.personnelCount = personnelCount;
        this.store = store;
        this.reservationStatus = ReservationStatus.PENDING;
    }


}
