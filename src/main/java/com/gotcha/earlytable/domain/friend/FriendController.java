package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.dto.FriendListResponseDto;
import com.gotcha.earlytable.domain.friend.dto.FriendResponseDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    /**
     * 나의 친구 목록 조회 API
     *
     * @param userDetails
     * @return List<FriendResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping
    public ResponseEntity<List<FriendListResponseDto>> GetFriendList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        List<FriendListResponseDto> friendListResponseDtoList = friendService.getFriendList(user);

        return ResponseEntity.status(HttpStatus.OK).body(friendListResponseDtoList);
    }

    /**
     * 친구 단일 조회 API
     *
     * @param userId
     * @return FriendResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/users/{userId}")
    public ResponseEntity<FriendResponseDto> GetFriend(@PathVariable Long userId) {
        FriendResponseDto getFriendResponseDto = friendService.getFriend(userId);

        return ResponseEntity.status(HttpStatus.OK).body(getFriendResponseDto);
    }

    /**
     * 친구 삭제 API
     * @param userId
     * @param userDetails
     * @return String
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteFriend(@PathVariable Long userId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        friendService.deleteFriend(userId, user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("친구 삭제가 완료되었습니다.");
    }
}
