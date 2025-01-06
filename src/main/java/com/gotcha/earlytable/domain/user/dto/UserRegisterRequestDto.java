package com.gotcha.earlytable.domain.user.dto;

import com.gotcha.earlytable.global.enums.Auth;
import lombok.Getter;

@Getter
public class UserRegisterRequestDto {

    private final String nickname;

    private final String email;

    private final String password;

    private final String phoneNumber;

    private final Auth auth;

    public UserRegisterRequestDto(String nickname, String email, String password, String phoneNumber, Auth auth) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.auth = auth;
    }
}
