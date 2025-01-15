package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WaitingOwnerResponseDto {
    private int waitingCount;
    private WaitingType waitingType;
    private List<WaitingSimpleResponseDto> waitingList;

    public WaitingOwnerResponseDto(List<Waiting> waitingList, WaitingType waitingType) {
        this.waitingCount = waitingList.size();
        this.waitingType = waitingType;
        this.waitingList = waitingList.stream()
                .map(WaitingSimpleResponseDto::new)
                .collect(Collectors.toList());
    }
}
