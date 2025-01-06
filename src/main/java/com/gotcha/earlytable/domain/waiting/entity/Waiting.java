package com.gotcha.earlytable.domain.waiting.entity;

import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "waiting")
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /*
    예약초대 아이디 작성
     */

    @Column(nullable = false)
    private WaitingType waitingType;

    @Column(nullable = false)
    private int personnelCount;

    @Column(nullable = false)
    private WaitingStatus waitingStatus;

    public Waiting(){}
}
