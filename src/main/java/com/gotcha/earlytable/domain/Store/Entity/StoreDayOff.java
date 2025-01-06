package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "store_day_off")
public class StoreDayOff extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeDayOffId;

    @Column(nullable = false)
    private LocalDate storeOffDay;

    private String StoreOffReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    public StoreDayOff(LocalDate storeOffDay, String storeOffReason, Store store) {
        this.storeOffDay = storeOffDay;
        this.StoreOffReason = storeOffReason;
        addStore(store);
    }

    public StoreDayOff() {

    }

    private void addStore(Store store) {
        this.store = store;
        store.getStoreDayOffList().add(this);
    }
}
