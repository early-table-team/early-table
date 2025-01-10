package com.gotcha.earlytable.domain.user.dto;

import lombok.Getter;

@Getter
public class UserPWRequestDto {

    private final String password;

    public UserPWRequestDto(String password) {
        this.password = password;
    }
}
