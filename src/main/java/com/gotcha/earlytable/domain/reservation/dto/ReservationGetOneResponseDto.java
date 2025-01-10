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

    private final LocalDateTime reservationDate;

    private final String storeName;

    private final ReservationStatus reservationStatus;

    private final Integer personnelCount;

    private final List<String> invitationPeople;

    private final String phoneNumber;

    private final List<ReturnMenuList> menuList;

    public ReservationGetOneResponseDto(Reservation reservation, User user, List<ReturnMenuList> menuList) {
        this.reservationId = reservation.getReservationId();
        this.reservationDate = reservation.getReservationDate().atTime(reservation.getReservationTime()); // 예약에 저장된 날짜와 시간을 조합하여 한번에 보여줌
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
