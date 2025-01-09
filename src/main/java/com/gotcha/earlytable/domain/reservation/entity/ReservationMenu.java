package com.gotcha.earlytable.domain.reservation.entity;

import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;



@Entity
@Getter
@Table(name = "reservation_menu")
public class ReservationMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationMenuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private Long menuCount;

    public ReservationMenu() {}


    public ReservationMenu(Menu menu, Reservation reservation, Long menuCount) {
        this.menu = menu;
        addReservation(reservation);
        this.menuCount = menuCount;
    }

    private void addReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.getReservationMenuList().add(this);
    }
}
