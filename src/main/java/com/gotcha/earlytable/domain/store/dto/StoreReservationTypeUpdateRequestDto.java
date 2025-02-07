package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.global.enums.WaitingType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StoreReservationTypeUpdateRequestDto {

    @NotNull
    private final ReservationType reservationType;

    private List<WaitingType> waitingType = new ArrayList<>();

    public StoreReservationTypeUpdateRequestDto(ReservationType reservationType, List<WaitingType> waitingType) {
        this.reservationType = reservationType;
        if (waitingType != null && !waitingType.isEmpty()) {
            this.waitingType = waitingType;
        }
    }
}
