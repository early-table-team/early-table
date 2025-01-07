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
@Table(name = "waiting")
public class Waiting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WaitingType waitingType;

    @Column(nullable = false)
    private Integer personnelCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    public Waiting() {
    }

    public Waiting(Store store, Party party, WaitingType waitingType,
                   Integer personnelCount, WaitingStatus waitingStatus) {
        addStore(store);
        addParty(party);
        this.waitingType = waitingType;
        this.personnelCount = personnelCount;
        this.waitingStatus = waitingStatus;
    }

    private void addStore(Store store) {
        this.store = store;
        store.getWaitingList().add(this);
    }

    private void addParty(Party party) {
        this.party = party;
        party.addWaiting(this);
    }
}
