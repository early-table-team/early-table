package com.gotcha.earlytable.domain.invitation.entity;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
@Table(name = "invitation")
public class Invitation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @OneToOne(mappedBy = "invitation")
    @JoinColumn(nullable = false)
    private Waiting waiting;

    @OneToOne(mappedBy = "invitation")
    @JoinColumn(nullable = false)
    private Reservation reservation;

    public Invitation() {}


    public void addWaiting(Waiting waiting) {
        this.waiting = waiting;
    }

    public void addReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
