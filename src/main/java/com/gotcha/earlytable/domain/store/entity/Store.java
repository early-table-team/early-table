package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.store.dto.StoreUpdateRequestDto;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "store")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable=false)
    private String storeName;

    @Column(nullable=false)
    private String storeTel;

    @Column(nullable=false)
    private String storeContents;

    @Column(nullable=false)
    private String storeAddress;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private StoreCategory storeCategory;

    private String RegionTop;

    private String RegionBottom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreKeyword> storeKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreRest> storeRestList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreHour> storeHourList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreReservationType> storeReservationTypeList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WaitingSetting> waitingSettingList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreTable> storeTableList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreTimeSlot> storeTimeSlotList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Waiting> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<>();

    public Store(String storeName, String storeTel, String storeContents, String storeAddress, StoreStatus storeStatus,
                 StoreCategory storeCategory, String regionTop, String regionBottom, User user, File file) {
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeContents = storeContents;
        this.storeAddress = storeAddress;
        this.storeStatus = storeStatus;
        this.storeCategory = storeCategory;
        this.RegionTop = regionTop;
        this.RegionBottom = regionBottom;
        this.user = user;
        this.file = file;
    }

    public Store() {

    }

    public void updateStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }


    public void updateStore(StoreUpdateRequestDto requestDto) {

        if(requestDto.getStoreName() != null) {
            this.storeName = requestDto.getStoreName();
        }
        if(requestDto.getStoreTel() != null) {
            this.storeTel = requestDto.getStoreTel();
        }
        if(requestDto.getStoreContents() != null) {
            this.storeContents = requestDto.getStoreContents();
        }
        if(requestDto.getStoreAddress() != null) {
            this.storeAddress = requestDto.getStoreAddress();
        }
        if(requestDto.getStoreCategory() != null) {
            this.storeCategory = requestDto.getStoreCategory();
        }
        if(requestDto.getRegionTop() != null) {
            this.RegionTop = requestDto.getRegionTop();
        }
        if(requestDto.getRegionBottom() != null) {
            this.RegionBottom = requestDto.getRegionBottom();
        }
    }

    public void updateStoreFromPendingStore(PendingStore pendingStore, File file) {

        if(pendingStore.getStoreName() != null) {
            this.storeName = pendingStore.getStoreName();
        }
        if(pendingStore.getStoreTel() != null) {
            this.storeTel = pendingStore.getStoreTel();
        }
        if(pendingStore.getStoreContents() != null) {
            this.storeContents = pendingStore.getStoreContents();
        }
        if(pendingStore.getStoreAddress() != null) {
            this.storeAddress = pendingStore.getStoreAddress();
        }
        if(pendingStore.getStoreCategory() != null) {
            this.storeCategory = pendingStore.getStoreCategory();
        }
        if(pendingStore.getRegionTop() != null) {
            this.RegionTop = pendingStore.getRegionTop();
        }
        if(pendingStore.getRegionBottom() != null) {
            this.RegionBottom = pendingStore.getRegionBottom();
        }
        if(file != null) {
            this.file = file;
        }
    }
}
