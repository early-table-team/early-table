package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "store_reservation_type")
public class StoreReservationType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeKeywordId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public StoreReservationType(ReservationType reservationType, Store store) {
        this.reservationType = reservationType;
        addStore(store);
    }

    public StoreReservationType() {

    }

    private void addStore(Store store) {
        this.store = store;
        store.getStoreReservationTypeList().add(this);
    }
}
