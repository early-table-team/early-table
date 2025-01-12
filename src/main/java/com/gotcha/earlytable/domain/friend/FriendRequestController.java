package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.dto.FriendRequestRequestDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestResponseDto;
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
@RequestMapping("/friends/request")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    /**
     * 친구 요청 보내기 API
     * @param friendRequestRequestDto
     * @param userDetails
     * @return FriendRequestResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping
    public ResponseEntity<FriendRequestResponseDto> createFriendRequest(@ModelAttribute FriendRequestRequestDto friendRequestRequestDto,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        FriendRequestResponseDto createFriendResponseDto = friendRequestService.createFriendRequest(user, friendRequestRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createFriendResponseDto);
    }

    /**
     * 친구 요청 수신 목록 조회 API
     * @param userDetails
     * @return List<FriendRequestResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping
    public ResponseEntity<List<FriendRequestResponseDto>> getFriendRequestList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        List<FriendRequestResponseDto> friendRequestResponseDtoList = friendRequestService.getFriendRequestList(user);

        return ResponseEntity.status(HttpStatus.OK).body(friendRequestResponseDtoList);
    }

    /**
     * 친구 요청 상태(수락/거절) 변경 API
     * @param friendRequestId
     * @param friendRequestRequestDto
     * @param userDetails
     * @return FriendRequestResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PatchMapping("/{friendRequestId}")
    public ResponseEntity<FriendRequestResponseDto> updateFriendRequestStatus(@PathVariable Long friendRequestId,
                                                                              @ModelAttribute FriendRequestRequestDto friendRequestRequestDto,
                                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        FriendRequestResponseDto updateFriendRequestResponseDto = friendRequestService.updateFriendRequestStatus(friendRequestId,user,friendRequestRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateFriendRequestResponseDto);
    }

    /**
     * 친구 요청 내역 삭제 API (ADMIN)
     * @param friendRequestRequestDto
     * @return String
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @DeleteMapping
    public ResponseEntity<String> deleteFriendRequest(@ModelAttribute FriendRequestRequestDto friendRequestRequestDto) {
        friendRequestService.deleteFriendRequest(friendRequestRequestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("친구 신청 내역이 삭제 완료되었습니다.");
    }
}
