package com.gotcha.earlytable.domain.friend.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.user.entity.User;
import lombok.Getter;

@Getter
public class FriendResponseDto {
    private final Long userId;

    private final String nickName;

    private final String email;

    private final String phoneNumber;

    private final String profileImageUrl;

    private final String relationship;


    public FriendResponseDto(User user, String relationship) {
        this.userId = user.getId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhone().substring(0, user.getPhone().length() - 4) + "****";
        this.profileImageUrl = user.getFile().getFileDetailList().stream().findFirst()
                .map(FileDetail::getFileUrl).orElse(null);
        this.relationship = relationship;
    }

}
