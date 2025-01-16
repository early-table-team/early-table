package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StoreReservationTypeDeleteRequestDto {

    @NotNull
    private final ReservationType reservationType;

    public StoreReservationTypeDeleteRequestDto(ReservationType reservationType) {
        this.reservationType = reservationType;
    }
}
