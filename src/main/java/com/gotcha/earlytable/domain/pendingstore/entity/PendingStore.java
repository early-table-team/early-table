package com.gotcha.earlytable.domain.pendingstore.entity;

import com.gotcha.earlytable.domain.pendingstore.enums.PendingStoreType;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "pending_store")
public class PendingStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pendingStoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String storeName;

    private String storeTel;

    private String storeContents;

    private String storeAddress;

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Enumerated(EnumType.STRING)
    private StoreCategory storeCategory;

    @Enumerated(EnumType.STRING)
    private RegionTop RegionTop;

    @Enumerated(EnumType.STRING)
    private RegionBottom RegionBottom;

    private Long fileId;

    private Long storeId;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private PendingStoreType pendingStoreType;



    public PendingStore(User user, String storeName, String storeTel,
                        String storeContents, String storeAddress, StoreStatus storeStatus,
                        StoreCategory storeCategory, RegionTop regionTop, RegionBottom regionBottom, Long fileId,
                        Long storeId, PendingStoreType pendingStoreType) {

        this.user = user;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeContents = storeContents;
        this.storeAddress = storeAddress;
        this.storeStatus = storeStatus;
        this.storeCategory = storeCategory;
        this.RegionTop = regionTop;
        this.RegionBottom = regionBottom;
        this.fileId = fileId;
        this.storeId = storeId;
        this.pendingStoreType = pendingStoreType;
    }

    public PendingStore() {

    }

    public void updateStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }
}
