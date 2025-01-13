package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.store.dto.StoreRestUpdateRequestDto;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "store_rest")
public class StoreRest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeRestId;

    @Column(nullable = false)
    private LocalDate storeOffDay;

    private String StoreOffReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    public StoreRest(LocalDate storeOffDay, String storeOffReason, Store store) {
        this.storeOffDay = storeOffDay;
        this.StoreOffReason = storeOffReason;
        addStore(store);
    }

    public StoreRest() {

    }

    private void addStore(Store store) {
        this.store = store;
        store.getStoreRestList().add(this);
    }


    public void updateStoreRest(StoreRestUpdateRequestDto requestDto) {

        if(requestDto.getStoreOffReason() != null) {
            this.StoreOffReason = requestDto.getStoreOffReason();
        }
    }
}
