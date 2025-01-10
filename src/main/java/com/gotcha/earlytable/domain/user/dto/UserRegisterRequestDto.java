package com.gotcha.earlytable.domain.user.dto;

import com.gotcha.earlytable.global.enums.Auth;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserRegisterRequestDto {

    private final String nickname;

    private final String email;

    private final String password;

    private final String phoneNumber;

    private final Auth auth;

    private final MultipartFile profileImage;

    public UserRegisterRequestDto(String nickname, String email, String password, String phoneNumber, Auth auth, MultipartFile profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.auth = auth;
        this.profileImage = profileImage;
    }
}
