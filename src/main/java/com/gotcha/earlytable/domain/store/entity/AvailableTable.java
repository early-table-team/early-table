package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "available_table")
public class AvailableTable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availableTableId;

    @Column(nullable = false)
    private LocalDate presentReservationDate;

    @Column(nullable = false)
    private Integer remainTable;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_master_id", nullable = false)
    private ReservationMaster reservationMaster;

    public AvailableTable(LocalDate presentReservationDate, Integer remainTable, ReservationMaster reservationMaster) {
        this.presentReservationDate = presentReservationDate;
        this.remainTable = remainTable;
        addReservationMaster(reservationMaster);
    }

    public AvailableTable() {

    }

    private void addReservationMaster(ReservationMaster reservationMaster) {
        this.reservationMaster = reservationMaster;
        reservationMaster.getAvailableTableList().add(this);
    }

    public void decreaseRemainTable(){
        if(this.remainTable > 0 ){
            this.remainTable--;
        }else{
            throw new BadRequestException(ErrorCode.NO_SEAT);
        }
    }

}
