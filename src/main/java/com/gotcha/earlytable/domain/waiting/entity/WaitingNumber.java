package com.gotcha.earlytable.domain.waiting.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "waiting_number")
public class WaitingNumber extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingNumberId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "waiting_id", nullable = false)
    private Waiting waiting;

    private int waitingNumber;

    public WaitingNumber(int waitingNumber) {
    }

    public WaitingNumber() {
    }

    public void updateWaitingNumber(int waitingNumber) {
        this.waitingNumber = waitingNumber;
    }

    public void addWaiting(Waiting waiting) {
        this.waiting = waiting;
    }
}
