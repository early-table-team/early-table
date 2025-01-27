package com.gotcha.earlytable.domain.waiting.dto;

import lombok.Getter;

@Getter
public class WaitingSequenceDto {

    private final Integer sequence;

    private final String estimatedTime;

    public WaitingSequenceDto(Integer sequence, Integer estimatedTime) {
        if (estimatedTime <= 5) {
            this.estimatedTime = "곧 입장 가능합니다.";
        } else {
            this.estimatedTime = estimatedTime + " 분";
        }
        this.sequence = sequence;
    }
}
