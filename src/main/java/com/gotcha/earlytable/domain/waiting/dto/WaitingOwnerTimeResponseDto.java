package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WaitingOwnerTimeResponseDto {
    private int waitingCount;
    private WaitingType waitingType;
    private List<Object> waitingList;
    private final Integer waitingNumber;

    public WaitingOwnerTimeResponseDto(List<Waiting> waitingList, WaitingType waitingType, String detail, Integer waitingNumber) {
        this.waitingCount = waitingList.size();
        this.waitingType = waitingType;
        if ("now".equals(detail)) {
            this.waitingList = waitingList.stream()
                    .map(WaitingSimpleResponseDto::new)
                    .collect(Collectors.toList());
        }else if ("detail".equals(detail)) {
            this.waitingList = waitingList.stream()
                    .map(WaitingDetailResponseDto::new)
                    .collect(Collectors.toList());
        }
        this.waitingNumber = waitingNumber;
    }
}
