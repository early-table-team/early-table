package com.gotcha.earlytable.domain.reservation.entity;


import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    private LocalDateTime reservationDateTime;

    @Column(nullable = false)
    private Integer personnelCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReservationMenu> reservationMenuList = new ArrayList<>();

    public Reservation() {
    }

    public Reservation(LocalDateTime reservationDateTime, Integer personnelCount, Store store, Party party) {
        this.reservationDateTime = reservationDateTime;
        this.personnelCount = personnelCount;
        this.store = store;
        addParty(party);
        this.reservationStatus = ReservationStatus.PENDING;
    }

    private void addParty(Party party) {
        this.party = party;
        party.addReservation(this);
    }
}
