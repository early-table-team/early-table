package com.gotcha.earlytable.domain.waiting.entity;

import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.RemoteStatus;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "waiting")
public class Waiting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id")
    private Party party;

    @OneToOne(mappedBy = "waiting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private OfflineUser offlineUser;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WaitingType waitingType;

    @Column(nullable = false)
    private Integer personnelCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RemoteStatus remoteStatus;

    @Column(nullable = false)
    private Integer waitingNumber;

    private String phone;

    public Waiting() {
    }

    public Waiting(Store store, Party party, WaitingType waitingType, Integer personnelCount,
                   WaitingStatus waitingStatus, RemoteStatus remoteStatus, Integer waitingNumber, String phone) {
        addStore(store);
        addParty(party);
        this.waitingType = waitingType;
        this.personnelCount = personnelCount;
        this.waitingStatus = waitingStatus;
        this.remoteStatus = remoteStatus;
        this.waitingNumber = waitingNumber;
        this.phone = phone;
    }


    private void addStore(Store store) {
        this.store = store;
        store.getWaitingList().add(this);
    }

    private void addParty(Party party) {
        this.party = party;
        party.addWaiting(this);
    }

    private void addOfflineUser(OfflineUser offlineUser) {
        this.offlineUser = offlineUser;
        offlineUser.addWaiting(this);
    }

    public void updateWaitingNumber(Integer waitingNumber) {
        this.waitingNumber = waitingNumber;
    }

    public void updateWaiting(WaitingStatus waitingStatus) {
        this.waitingStatus = waitingStatus;
    }


    public void cancelWaiting() {
        this.waitingStatus = WaitingStatus.CANCELED;
    }
}
