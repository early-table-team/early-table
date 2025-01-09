package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "store_time_slot")
public class StoreTimeSlot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long StoreTimeSlotId;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_table_id")
    private StoreTable storeTable;


    public StoreTimeSlot(LocalTime reservationTime, StoreTable storeTable) {
        this.reservationTime = reservationTime;
        addStoreTable(storeTable);
    }

    public StoreTimeSlot() {

    }

    public void addStoreTable(StoreTable storeTable){
        this.storeTable = storeTable;
        storeTable.getStoreTimeSlotList().add(this);
    }

}
