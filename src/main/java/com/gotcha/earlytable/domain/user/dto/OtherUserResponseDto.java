package com.gotcha.earlytable.domain.user.dto;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Auth;
import lombok.Getter;

@Getter
public class OtherUserResponseDto {

    private final Long id;

    private final String nickname;

    private final String email;

    private final String phoneNumber;

    private final Auth auth;

    private final String imageUrl;

    private final String relationship;

    public OtherUserResponseDto(Long id, String nickname, String email, String phoneNumber, Auth auth, String imageUrl, String relationship) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.auth = auth;
        this.imageUrl = imageUrl;
        this.relationship = relationship;
    }

    public static OtherUserResponseDto toDto(User user, String imageUrl, String relationship) {
        return new OtherUserResponseDto(
                user.getId(),
                user.getNickName(),
                user.getEmail(), user.getPhone().substring(0, user.getPhone().length() - 4) + "****",
                user.getAuth(),
                imageUrl,
                relationship
        );
    }
}
