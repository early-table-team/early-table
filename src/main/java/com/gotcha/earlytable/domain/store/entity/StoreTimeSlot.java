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
    @JoinColumn(name = "store_id")
    private Store store;


    public StoreTimeSlot(LocalTime reservationTime, Store store) {
        this.reservationTime = reservationTime;
        addStore(store);
    }

    public StoreTimeSlot() {

    }

    public void addStore(Store store){
        this.store = store;
        store.getStoreTimeSlotList().add(this);
    }

    public void changeTimeSlot(LocalTime reservationTime){
        this.reservationTime = reservationTime;
    }

}
