package com.gotcha.earlytable.domain.store.entity;

import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
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

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreKeyword> storeKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreDayOff> storeDayOffList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreHour> storeHourOffList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreReservationType> storeReservationTypeList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WaitingSetting> waitingSettingList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReservationMaster> reservationMasterList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Waiting> waitingList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<>();

    public Store(String storeName, String storeTel, String storeContents, String storeAddress, StoreStatus storeStatus,
                 StoreCategory storeCategory, String regionTop, String regionBottom, User user) {
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.storeContents = storeContents;
        this.storeAddress = storeAddress;
        this.storeStatus = storeStatus;
        this.storeCategory = storeCategory;
        this.RegionTop = regionTop;
        this.RegionBottom = regionBottom;
        this.user = user;
    }

    public Store() {

    }

}
