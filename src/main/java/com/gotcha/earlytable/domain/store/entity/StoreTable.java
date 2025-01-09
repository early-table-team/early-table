package com.gotcha.earlytable.domain.store.entity;


import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "store_table")
public class StoreTable  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeTableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Integer tableMaxNumber;

    private Integer tableCount;



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
