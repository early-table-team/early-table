package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "waiting_setting")
public class WaitingSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingSettingId;

    @Column(nullable = false)
    private LocalTime waitingOpenTime;


    @Column(nullable = false)
    private LocalTime waitingClosedTime;

    @Column(nullable = false)
    private WaitingStatus waitingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public WaitingSetting(LocalTime waitingOpenTime, LocalTime waitingClosedTime, WaitingStatus waitingStatus, Store store) {
        this.waitingOpenTime = waitingOpenTime;
        this.waitingClosedTime = waitingClosedTime;
        this.waitingStatus = waitingStatus;
        addStore(store);
    }

    public WaitingSetting() {

    }

    private void addStore(Store store) {
        this.store = store;
        store.getWaitingSettingList().add(this);
    }
}
