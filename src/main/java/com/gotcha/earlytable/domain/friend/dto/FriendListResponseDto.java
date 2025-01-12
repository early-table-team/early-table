package com.gotcha.earlytable.domain.friend.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.friend.entity.Friend;
import lombok.Getter;

@Getter
public class FriendListResponseDto {

    private final Long userId;

    private final String nickName;

    private final String profileImageUrl;

    public FriendListResponseDto(Long userId, String nickName, String profileImageUrl) {
        this.userId = userId;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
    }


    public static FriendListResponseDto toDto(Friend friend) {
        return new FriendListResponseDto(
                friend.getReceivedUser().getId(),
                friend.getReceivedUser().getNickName(),
                friend.getReceivedUser().getFile().getFileDetailList().stream().findFirst()
                        .map(FileDetail::getFileUrl).orElse(null)
        );
    }
}
