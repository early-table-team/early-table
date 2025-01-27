package com.gotcha.earlytable.domain.user.dto;

import lombok.Getter;

@Getter
public class UserReservationCountResponseDto {

    private final Long reservationCount;
    private final Long waitingCount;

    public UserReservationCountResponseDto(Long reservationCount, Long waitingCount) {
        this.reservationCount = reservationCount;
        this.waitingCount = waitingCount;
    }
}
