package com.gotcha.earlytable.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class TokenSaveRequestDto {
    private String token;

    @JsonCreator
    public TokenSaveRequestDto(String token) {
        this.token = token;
    }
}
