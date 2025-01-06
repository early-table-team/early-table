package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "reservation_master")
public class ReservationMaster extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationMasterId;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @Column(nullable = false)
    private Integer tableMaxNumber;

    @Column(nullable = false)
    private Integer tableCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "reservationMaster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AvailableTable> availableTableList = new ArrayList<>();

    public ReservationMaster(LocalTime reservationTime, Integer tableMaxNumber, Integer tableCount, Store store) {
        this.reservationTime = reservationTime;
        this.tableMaxNumber = tableMaxNumber;
        this.tableCount = tableCount;
        addStore(store);
    }

    public ReservationMaster() {

    }

    private void addStore(Store store) {
        this.store = store;
        store.getReservationMasterList().add(this);
    }
}
