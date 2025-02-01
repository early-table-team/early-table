package com.gotcha.earlytable.domain.store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreRestDateResponseDto {
    private final List<String> restDates;
    private final List<Integer> restWeekDay;

    public StoreRestDateResponseDto(List<String> restDates, List<Integer> restWeekDay) {
        this.restDates = restDates;
        this.restWeekDay = restWeekDay;
    }
}
