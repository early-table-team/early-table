package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "store_reservation_type")
public class StoreReservationType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeReservationTypeId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;

    @Column(nullable = false)
    private boolean toGo = false;

    @Column(nullable = false)
    private boolean dineIn = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;

    public StoreReservationType(ReservationType reservationType, List<WaitingType> waitingTypes, Store store) {
        this.reservationType = reservationType;
        this.toGo = waitingTypes.contains(WaitingType.TOGO);
        this.dineIn = waitingTypes.contains(WaitingType.DINE);
        addStore(store);
    }

    public void updateReservationType(List<WaitingType> waitingTypes) {
        this.toGo = waitingTypes.contains(WaitingType.TOGO);
        this.dineIn = waitingTypes.contains(WaitingType.DINE);
    }

    public StoreReservationType() {

    }

    public boolean canWaiting(WaitingType waitingType) {
        if (waitingType == WaitingType.DINE) {
            return dineIn;
        } else {
            return toGo;
        }

    }

    private void addStore(Store store) {
        this.store = store;
        store.getStoreReservationTypeList().add(this);
    }
}
