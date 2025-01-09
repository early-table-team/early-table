package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.dto.FriendResponseDto;
import com.gotcha.earlytable.domain.friend.entity.Friend;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    /**
     * 나의 친구 목록 조회 서비스 메서드
     */
    public List<FriendResponseDto> getFriendList(User user) {
        List<Friend> friendList = friendRepository.findBySendUserId(user.getId());

        return friendList.stream().map(FriendResponseDto::toDto).toList();
    }

    /**
     * 친구 단일 조회 서비스 메서드
     */
    public FriendResponseDto getFriend(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        return new FriendResponseDto(user);
    }
}
