package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.ReservationType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreReservationTypeRequestDto {

    @NotNull
    private final ReservationType reservationType;

    public StoreReservationTypeRequestDto(ReservationType reservationType) {

        this.reservationType = reservationType;
    }
}
