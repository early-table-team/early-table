package com.gotcha.earlytable.domain.waitingsetting.entity;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waitingsetting.dto.WaitingSettingRequestDto;
import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import com.gotcha.earlytable.global.base.BaseEntity;
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
    private WaitingSettingStatus waitingSettingStatus;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;

    public WaitingSetting(LocalTime waitingOpenTime, LocalTime waitingClosedTime, WaitingSettingStatus waitingSettingStatus, Store store) {
        this.waitingOpenTime = waitingOpenTime;
        this.waitingClosedTime = waitingClosedTime;
        this.waitingSettingStatus = waitingSettingStatus;
        addStore(store);
    }

    public WaitingSetting() {

    }

    private void addStore(Store store) {
        this.store = store;
//        store.getWaitingSettingList().add(this);
    }

    public void updateWaitingSetting(WaitingSettingRequestDto requestDto) {

        if(requestDto.getWaitingOpenTime() != null) {
            this.waitingOpenTime = requestDto.getWaitingOpenTime();
        }
        if(requestDto.getWaitingClosedTime() != null) {
            this.waitingClosedTime = requestDto.getWaitingClosedTime();
        }
        if(requestDto.getWaitingSettingStatus() != null) {
            this.waitingSettingStatus = requestDto.getWaitingSettingStatus();
        }
    }

    public void updateStatus(WaitingSettingStatus waitingSettingStatus) {
        this.waitingSettingStatus = waitingSettingStatus;
    }
}
