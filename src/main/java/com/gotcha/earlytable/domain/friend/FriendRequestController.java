package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.dto.FriendRequestDeleteRequestDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestRequestDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestResponseDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestUpdateRequestDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
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
     *
     * @param requestDto (receivedUserId)
     * @param userDetails
     * @return FriendRequestResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping
    public ResponseEntity<String> createFriendRequest(@Valid @RequestBody FriendRequestRequestDto requestDto,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        String message = friendRequestService.createFriendRequest(user, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    /**
     * 친구 요청 수신 목록 조회 API
     *
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
     *
     * @param friendRequestId
     * @param requestDto (invitationStatus)
     * @param userDetails
     * @return ResponseEntity<FriendRequestResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PatchMapping("/{friendRequestId}")
    public ResponseEntity<String> updateFriendRequestStatus(@PathVariable Long friendRequestId,
                                                                              @Valid @RequestBody FriendRequestUpdateRequestDto requestDto,
                                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        String message = friendRequestService.updateFriendRequestStatus(friendRequestId, requestDto, user);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * 친구 요청 취소 API
     *
     * @param friendRequestId
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("/{friendRequestId}/cancel")
    public ResponseEntity<String> cancelFriendRequest(@PathVariable Long friendRequestId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        friendRequestService.cancelFriendRequest(friendRequestId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("친구 신청 취소 완료되었습니다.");

    }

    /**
     * 친구 요청 내역 삭제 API (ADMIN)
     *
     * @param requestDto
     * @return ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @DeleteMapping
    public ResponseEntity<String> deleteFriendRequest(@Valid @RequestBody FriendRequestDeleteRequestDto requestDto) {

        friendRequestService.deleteFriendRequest(requestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("친구 신청 내역이 삭제 완료되었습니다.");
    }
}
