package com.gotcha.earlytable.domain.reservation.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Getter
public class ReservationCreateResponseDto {

    private final Long reservationId;

    private final ReservationCreateRequestDto reservationCreateRequestDto;

    public ReservationCreateResponseDto(Long reseravtionId, ReservationCreateRequestDto reservationCreateRequestDto) {
        this.reservationId = reseravtionId;
        this.reservationCreateRequestDto = reservationCreateRequestDto;
    }
}
