package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.ReservationType;
import lombok.Getter;

@Getter
public class StoreReservationTypeRequestDto {

    private final ReservationType reservationType;

    public StoreReservationTypeRequestDto(ReservationType reservationType) {

        this.reservationType = reservationType;
    }
}
