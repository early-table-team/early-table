package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReservationGetOneResponseDto {

    private final Long reservationId;


    private final String storeName;

    private final ReservationStatus reservationStatus;

    private final Integer personnelCount;

    private final List<String> invitationPeople;

    private final String phoneNumber;

    private final List<HashMap<String, Long>> menuList;

    public ReservationGetOneResponseDto(Reservation reservation, User user, List<HashMap<String, Long>> menuList) {
        this.reservationId = reservation.getReservationId();
        this.storeName = reservation.getStore().getStoreName();
        this.reservationStatus = reservation.getReservationStatus();
        this.personnelCount = reservation.getPersonnelCount();
        this.invitationPeople = reservation.getParty().getPartyPeople().stream()
                .map(partyPeople -> partyPeople.getUser().getNickName())
                .collect(Collectors.toList());
        this.phoneNumber = user.getPhone();
        this.menuList = menuList;
    }
}
