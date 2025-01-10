package com.gotcha.earlytable.domain.user.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserUpdateRequestDto {

    private final String nickname;

    private final String phoneNumber;

    private final MultipartFile profileImage;

    public UserUpdateRequestDto(String nickname, String phoneNumber, MultipartFile profileImage) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }
}
