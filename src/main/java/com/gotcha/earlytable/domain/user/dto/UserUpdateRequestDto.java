package com.gotcha.earlytable.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserUpdateRequestDto {

    private final String nickname;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "유효한 핸드폰 번호를 입력하세요.")
    private final String phoneNumber;

    private final MultipartFile profileImage;

    public UserUpdateRequestDto(String nickname, String phoneNumber, MultipartFile profileImage) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }
}
