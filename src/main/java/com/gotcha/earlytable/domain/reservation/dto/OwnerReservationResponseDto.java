package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class OwnerReservationResponseDto {

    private Long reservationId;

    private LocalDate reservationDate;

    private LocalTime reservationTime;

    private String representativeName;

    private Integer personnelCount;

    private String phoneNumber;

    private String reservationStatus;

    public OwnerReservationResponseDto(Reservation reservation) {
        this.reservationId = reservation.getReservationId();
        this.reservationDate = reservation.getReservationDate();
        this.reservationTime = reservation.getReservationTime();
        this.representativeName = (reservation.getParty().getPartyPeople().stream().filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE))
                .map(partyPeople -> partyPeople.getUser().getNickName()) // 유저의 이름 추출
                .findFirst() // 첫 번째 요소 가져오기
                .orElse(null));
        this.personnelCount = reservation.getPersonnelCount();
        this.phoneNumber = reservation.getParty().getPartyPeople().stream().filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE))
                .map(partyPeople -> partyPeople.getUser().getPhone())
                .findFirst().orElse(null);
        this.reservationStatus = reservation.getReservationStatus().getValue();

    }
}