package com.gotcha.earlytable.domain.user.dto;

import lombok.Getter;

@Getter
public class UserDeleteRequestDto {

    private final String password;

    public UserDeleteRequestDto(String password) {
        this.password = password;
    }
}
