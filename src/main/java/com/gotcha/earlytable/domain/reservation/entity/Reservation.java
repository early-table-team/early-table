package com.gotcha.earlytable.domain.reservation.entity;


import com.gotcha.earlytable.domain.invitation.entity.Invitation;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
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
    private LocalDate reservationDate;

    @Column(nullable = false)
    private Integer personnelCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "invitation_id", nullable = false)
    private Invitation invitation;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReservationMenu> reservationMenuList = new ArrayList<>();

    public Reservation() {}

    public Reservation(LocalDate reservationDate, Integer personnelCount, Store store,Invitation invitation) {
        this.reservationDate = reservationDate;
        this.personnelCount = personnelCount;
        this.store = store;
        addInvitation(invitation);
        this.reservationStatus = ReservationStatus.PENDING;
    }

    private void addInvitation(Invitation invitation) {
        this.invitation = invitation;
        invitation.addReservation(this);
    }
}
