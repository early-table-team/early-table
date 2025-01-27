package com.gotcha.earlytable.domain.user.dto;

import com.gotcha.earlytable.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSearchResponseDto {
    private final Long id;
    private final String nickname;
    private final String phoneNumberBottom;
    private final String email;

    public UserSearchResponseDto(Long id, String nickname, String phoneNumberBottom, String email) {
        this.id = id;
        this.nickname = nickname;
        this.phoneNumberBottom = phoneNumberBottom;
        this.email = email;
    }

    public static UserSearchResponseDto toDto(User user) {
        return new UserSearchResponseDto(
                user.getId(),
                user.getNickName(),
                user.getPhone().substring(9,13),
                user.getEmail()
        );
    }
}
