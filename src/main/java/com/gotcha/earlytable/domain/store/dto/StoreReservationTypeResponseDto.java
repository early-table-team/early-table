package com.gotcha.earlytable.domain.store.dto;

import com.gotcha.earlytable.domain.store.entity.StoreReservationType;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import lombok.Getter;

@Getter
public class StoreReservationTypeResponseDto {
    private final Long storeReservationTypeId;
    private final ReservationType reservationType;
    private final boolean toGo;
    private final boolean dineIn;

    public StoreReservationTypeResponseDto(Long storeReservationTypeId, ReservationType reservationType, boolean toGo, boolean dineIn) {
        this.storeReservationTypeId = storeReservationTypeId;
        this.reservationType = reservationType;
        this.toGo = toGo;
        this.dineIn = dineIn;
    }

    public static StoreReservationTypeResponseDto toDto(Long storeReservationTypeId, ReservationType reservationType, boolean toGo, boolean dineIn) {
        return new StoreReservationTypeResponseDto(
                storeReservationTypeId,
                reservationType,
                toGo,
                dineIn
        );
    }

    public static StoreReservationTypeResponseDto toDto(StoreReservationType storeReservationType) {
        return new StoreReservationTypeResponseDto(
                storeReservationType.getStoreReservationTypeId(),
                storeReservationType.getReservationType(),
                storeReservationType.isToGo(),
                storeReservationType.isDineIn()
        );
    }
}
