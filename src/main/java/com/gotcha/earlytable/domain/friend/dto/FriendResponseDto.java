package com.gotcha.earlytable.domain.friend.dto;

import com.gotcha.earlytable.domain.friend.entity.Friend;
import com.gotcha.earlytable.domain.user.entity.User;
import lombok.Getter;

@Getter
public class FriendResponseDto {
    private Long userId;
    private String nickName;
    private String email;
    private String phoneNumber;
    //imageFile

    public FriendResponseDto(Long userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
    }

    public FriendResponseDto(User user) {
        this.userId = user.getId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhone();
    }

    public static FriendResponseDto toDto(Friend friend) {
        return new FriendResponseDto(
            friend.getReceivedUser().getId(),
            friend.getReceivedUser().getNickName()
        );
    }
}
