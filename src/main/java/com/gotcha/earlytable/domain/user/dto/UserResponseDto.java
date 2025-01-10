package com.gotcha.earlytable.domain.user.dto;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Auth;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;

    private final String nickname;

    private final String email;

    private final String phoneNumber;

    private final Auth auth;

    private final String imageUrl;

    public UserResponseDto(Long id, String nickname, String email, String phoneNumber, Auth auth, String imageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.auth = auth;
        this.imageUrl = imageUrl;
    }

    public static UserResponseDto toDto(User user, String imageUrl) {
        return new UserResponseDto(
                user.getId(),
                user.getNickName(),
                user.getEmail(),
                user.getPhone(),
                user.getAuth(),
                imageUrl
        );
    }
}
