package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreReservationTypeRequestDto {

    @NotNull
    private final ReservationType reservationType;

    @NotNull
    private final List<WaitingType> waitingType;

    public StoreReservationTypeRequestDto(ReservationType reservationType, List<WaitingType> waitingType) {

        this.reservationType = reservationType;
        this.waitingType = waitingType;
    }
}
