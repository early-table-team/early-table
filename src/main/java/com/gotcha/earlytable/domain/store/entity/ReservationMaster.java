package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "reservation_master")
public class ReservationMaster extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationMasterId;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_table_id")
    private StoreTable storeTable;

    @OneToMany(mappedBy = "reservationMaster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AvailableTable> availableTableList = new ArrayList<>();


    public ReservationMaster(LocalTime reservationTime, StoreTable storeTable) {
        this.reservationTime = reservationTime;
        addStoreTable(storeTable);
    }

    public ReservationMaster() {

    }

    public void addStoreTable(StoreTable storeTable){
        this.storeTable = storeTable;
        storeTable.getReservationMasterList().add(this);
    }

}
