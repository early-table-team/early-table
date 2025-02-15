package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.review.entity.Review;
import com.gotcha.earlytable.domain.store.dto.StoreUpdateRequestDto;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "store", indexes = {
        @Index(name = "idx_store_name", columnList = "storeName"),
        @Index(name = "idx_region_top", columnList = "regionTop"),
        @Index(name = "idx_region_bottom", columnList = "regionBottom"),
        @Index(name = "idx_store_category", columnList = "storeCategory")
})
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String storeTel;

    @Column(nullable = false)
    private String storeContents;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreCategory storeCategory;

    @Enumerated(EnumType.STRING)
    private RegionTop regionTop;

    @Enumerated(EnumType.STRING)
    private RegionBottom regionBottom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<StoreKeyword> storeKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<StoreRest> storeRestList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<StoreHour> storeHourList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<StoreReservationType> storeReservationTypeList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<StoreTable> storeTableList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<StoreTimeSlot> storeTimeSlotList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Waiting> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Reservation> reservationList = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Review> reviewList = new ArrayList<>();

    public Store(String storeName, String storeTel, String storeContents, String storeAddress, StoreStatus storeStatus,
                 StoreCategory storeCategory, RegionTop regionTop, RegionBottom regionBottom, User user, File file) {
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeContents = storeContents;
        this.storeAddress = storeAddress;
        this.storeStatus = storeStatus;
        this.storeCategory = storeCategory;
        this.regionTop = regionTop;
        this.regionBottom = regionBottom;
        this.user = user;
        this.file = file;
    }

    public Store() {

    }

    public void updateStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }


    public void updateStore(StoreUpdateRequestDto requestDto) {

        if (requestDto.getStoreName() != null && !requestDto.getStoreName().isEmpty()) {
            this.storeName = requestDto.getStoreName();
        }
        if (requestDto.getStoreTel() != null && !requestDto.getStoreTel().isEmpty()) {
            this.storeTel = requestDto.getStoreTel();
        }
        if (requestDto.getStoreContents() != null && !requestDto.getStoreContents().isEmpty()) {
            this.storeContents = requestDto.getStoreContents();
        }
        if (requestDto.getStoreAddress() != null && !requestDto.getStoreAddress().isEmpty()) {
            this.storeAddress = requestDto.getStoreAddress();
        }
        if (requestDto.getStoreCategory() != null) {
            this.storeCategory = requestDto.getStoreCategory();
        }
        if (requestDto.getRegionTop() != null) {
            this.regionTop = requestDto.getRegionTop();
        }
        if (requestDto.getRegionBottom() != null) {
            this.regionBottom = requestDto.getRegionBottom();
        }
    }

    public void updateStoreFromPendingStore(PendingStore pendingStore, File file) {

        if (pendingStore.getStoreName() != null && !pendingStore.getStoreName().isEmpty()) {
            this.storeName = pendingStore.getStoreName();
        }
        if (pendingStore.getStoreTel() != null && !pendingStore.getStoreTel().isEmpty()) {
            this.storeTel = pendingStore.getStoreTel();
        }
        if (pendingStore.getStoreContents() != null && !pendingStore.getStoreContents().isEmpty()) {
            this.storeContents = pendingStore.getStoreContents();
        }
        if (pendingStore.getStoreAddress() != null && !pendingStore.getStoreAddress().isEmpty()) {
            this.storeAddress = pendingStore.getStoreAddress();
        }
        if (pendingStore.getStoreCategory() != null) {
            this.storeCategory = pendingStore.getStoreCategory();
        }
        if (pendingStore.getRegionTop() != null) {
            this.regionTop = pendingStore.getRegionTop();
        }
        if (pendingStore.getRegionBottom() != null) {
            this.regionBottom = pendingStore.getRegionBottom();
        }
        if (file != null) {
            this.file = file;
        }
    }
}
