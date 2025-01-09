package com.gotcha.earlytable.domain.store.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "store_table")
public class StoreTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeTableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Integer tableMaxNumber;

    private Integer tableCount;

    @OneToMany(mappedBy = "storeTable", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReservationMaster> reservationMasterList = new ArrayList<>();


    public StoreTable() {}

    public StoreTable(Store store, Integer tableMaxNumber, Integer tableCount) {
        addStore(store);
        this.tableMaxNumber = tableMaxNumber;
        this.tableCount = tableCount;
    }

    private void addStore(Store store) {
        this.store = store;
        store.getStoreTableList().add(this);
    }

    public void changeTableMaxNumber(Integer tableMaxNumber) {
        this.tableMaxNumber = tableMaxNumber;
    }

    public void changeTableCount(Integer tableCount) {
        this.tableCount = tableCount;
    }
}
