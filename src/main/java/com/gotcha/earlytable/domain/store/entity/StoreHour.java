package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.store.dto.StoreHourUpdateRequestDto;
import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;


@Getter
@Entity
@Table(name = "store_hour")
public class StoreHour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeHourId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closedTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayStatus dayStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;


    public StoreHour(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closedTime, DayStatus dayStatus, Store store) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.dayStatus = dayStatus;
        addStore(store);
    }

    public StoreHour() {

    }

    private void addStore(Store store) {
        this.store = store;
        store.getStoreHourList().add(this);
    }

    public void updateStoreHour(StoreHourUpdateRequestDto requestDto) {
        if (requestDto.getOpenTime() != null) {
            this.openTime = requestDto.getOpenTime();
        }
        if (requestDto.getClosedTime() != null) {
            this.closedTime = requestDto.getClosedTime();
        }
        if (requestDto.getDayStatus() != null) {
            this.dayStatus = requestDto.getDayStatus();
        }
    }
}
