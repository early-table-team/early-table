package com.gotcha.earlytable.domain.waiting.entity;

import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "offline_user")
public class OfflineUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "waiting_id", nullable = false)
    private Waiting waiting;

    @Column(nullable = false)
    private String phoneNumber;

    public OfflineUser() {
    }

    public OfflineUser(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addWaiting(Waiting waiting) {
        this.waiting = waiting;
    }

}
