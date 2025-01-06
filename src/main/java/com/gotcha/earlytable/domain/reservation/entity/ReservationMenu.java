package com.gotcha.earlytable.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;



@Entity
@Getter
@Table(name = "reservation_menu")
public class ReservationMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationMenuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menus menuId;  //Menu클래스가 없어서 Menu를 적으면 자동추가 임포트때문에 임의로 Menus로 지정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservationId;

    public ReservationMenu() {}
}
