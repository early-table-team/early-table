package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.dto.FriendListResponseDto;
import com.gotcha.earlytable.domain.friend.dto.FriendResponseDto;
import com.gotcha.earlytable.domain.friend.entity.Friend;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<FriendListResponseDto> getFriendList(User user) {
        List<Friend> friendList = friendRepository.findBySendUserId(user.getId());

        return friendList.stream().map(FriendListResponseDto::toDto).toList();
    }

    /**
     * 친구 단일 조회 서비스 메서드
     */
    public FriendResponseDto getFriend(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        return new FriendResponseDto(user);
    }

    /**
     * 친구 삭제 서비스 메서드
     */
    @Transactional
    public void deleteFriend(Long userId, User user) {
        if(!friendRepository.existsBySendUserIdAndReceivedUserId(userId, user.getId())) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        friendRepository.deleteBySendUserIdAndReceivedUserId(userId, user.getId());
        friendRepository.deleteBySendUserIdAndReceivedUserId(user.getId(), userId);
    }
}
