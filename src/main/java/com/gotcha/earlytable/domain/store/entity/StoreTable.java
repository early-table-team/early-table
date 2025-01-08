package com.gotcha.earlytable.domain.store.entity;


import jakarta.persistence.*;
import lombok.Getter;

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

    public StoreTable() {}

    public StoreTable(Store store, Integer tableMaxNumber, Integer tableCount) {
        this.store = store;
        this.tableMaxNumber = tableMaxNumber;
        this.tableCount = tableCount;
    }


}
