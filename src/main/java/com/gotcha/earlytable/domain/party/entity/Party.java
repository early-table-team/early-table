package com.gotcha.earlytable.domain.party.entity;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;


@Entity
@Getter
@Table(name = "party")
public class Party extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;

    @OneToOne(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Waiting waiting;

    @OneToOne(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Reservation reservation;

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PartyPeople> partyPeople;


    public Party() {
    }


    public void addWaiting(Waiting waiting) {
        this.waiting = waiting;
    }

    public void addReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
