package com.gotcha.earlytable.domain.user.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserSearchResponseDto {
    private final Long id;
    private final String nickname;
    private final String phoneNumberBottom;
    private final String email;
    private final String profileImageUrl;

    public UserSearchResponseDto(Long id, String nickname, String phoneNumberBottom, String email, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.phoneNumberBottom = phoneNumberBottom;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserSearchResponseDto toDto(User user) {
        return new UserSearchResponseDto(
                user.getId(),
                user.getNickName(),
                user.getPhone().substring(user.getPhone().length() - 4),
                user.getEmail(),
                user.getFile().getFileDetailList().stream().findFirst()
                        .map(FileDetail::getFileUrl).orElse(null)
        );
    }
}
