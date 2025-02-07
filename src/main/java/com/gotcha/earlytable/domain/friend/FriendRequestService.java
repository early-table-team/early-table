package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.dto.FriendRequestDeleteRequestDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestRequestDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestResponseDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestUpdateRequestDto;
import com.gotcha.earlytable.domain.friend.entity.Friend;
import com.gotcha.earlytable.domain.friend.entity.FriendRequest;
import com.gotcha.earlytable.domain.notification.FcmService;
import com.gotcha.earlytable.domain.notification.SseEmitterService;
import com.gotcha.earlytable.domain.notification.dto.TokenNotificationRequestDto;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import com.gotcha.earlytable.global.enums.NotificationType;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.ForbiddenException;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final SseEmitterService sseEmitterService;
    private final FcmService fcmService;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, FriendRepository friendRepository,
                                UserRepository userRepository, SseEmitterService sseEmitterService, FcmService fcmService) {

        this.friendRequestRepository = friendRequestRepository;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.sseEmitterService = sseEmitterService;
        this.fcmService = fcmService;
    }

    /**
     * 친구 요청 보내기 서비스 메서드
     */
    @Transactional
    public String createFriendRequest(User user, FriendRequestRequestDto requestDto) {

        // 자기 자신에게 친구 요청 보낼 때 예외처리
        if(user.getId().equals(requestDto.getReceivedUserId())) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        //친구의 user 정보 찾기
        User receivedUser = userRepository.findByIdOrElseThrow(requestDto.getReceivedUserId());

        //이미 친구관계일 때 예외처리
        if(friendRepository.existsBySendUserIdAndReceivedUserId(user.getId(), requestDto.getReceivedUserId())) {
            throw new ConflictException(ErrorCode.ALREADY_IN_FRIEND);
        }

        //상대가 보낸 요청이 이미 존재할 때 -> 존재하는 요청 건 수락처리, 신규 요청은 생성하지 않음
        if(friendRequestRepository.isExistsFriendRequest(receivedUser.getId(), user.getId(), InvitationStatus.PENDING)) {

            //이미 존재하는 요청건 찾아오기
            FriendRequest friendRequest =
                    friendRequestRepository.findBySendUserIdAndReceivedUserIdAndInvitationStatus(receivedUser.getId(),
                                                                                                 user.getId(),
                                                                                                 InvitationStatus.PENDING);

            //상대가 보낸 요청 수락(->친구등록) 처리
            FriendRequestUpdateRequestDto friendRequestUpdateRequestDto = new FriendRequestUpdateRequestDto(InvitationStatus.ACCEPTED);
            return this.updateFriendRequestStatus(friendRequest.getFriendRequestId(), friendRequestUpdateRequestDto, user);
        }

        //내가 보낸 대기상태인 요청 건 존재시 예외처리
        if (friendRequestRepository.isExistsFriendRequest(user.getId(), receivedUser.getId(), InvitationStatus.PENDING)) {
            throw new ConflictException(ErrorCode.ALREADY_REQUESTED);
        }

        //거절상태인 요청 건 5건 이상 존재할 때 예외처리
        if(friendRequestRepository.countBySendUserIdAndReceivedUserIdAndInvitationStatus(user.getId(), receivedUser.getId(), InvitationStatus.REJECTED) >= 5) {
            throw new BadRequestException(ErrorCode.NO_MORE_REQUEST_AVAILABLE);
        }

        //친구 요청 등록
        FriendRequest friendRequest = new FriendRequest(user,receivedUser, InvitationStatus.PENDING);

        friendRequestRepository.save(friendRequest);

        // 알림 보내기
        sseEmitterService.send(receivedUser, user.getNickName() + "님에게 친구 요청이 왔습니다.", NotificationType.FRIEND);
        fcmService.sendNotificationByToken("친구 요청", user.getNickName()+"님에게 친구요청이 왔습니다.", "", receivedUser);

        return "친구 요청이 성공하였습니다.";
    }

    /**
     * 친구 요청 수신 목록 조회 서비스 메서드
     */
    public List<FriendRequestResponseDto> getFriendRequestList(User user) {

        List<FriendRequest> responsedtoList =
                friendRequestRepository.findByReceivedUserIdAndInvitationStatus(user.getId(), InvitationStatus.PENDING);

        return responsedtoList.stream().map(FriendRequestResponseDto::toDto).toList();
    }

    /**
     * 친구 요청 상태(수락/거절) 변경 서비스 메서드
     */
    @Transactional
    public String updateFriendRequestStatus(Long friendRequestId, FriendRequestUpdateRequestDto requestDto, User user) {


        // 상태 변경할 요청 건, 보내는사람, 받는사람 정보 받아오기
        FriendRequest friendRequest = friendRequestRepository.findByIdOrElseThrow(friendRequestId);

        User receivedUser = friendRequest.getReceivedUser();
        User sendUser = friendRequest.getSendUser();

        // 자신에게 온 요청이 아니면 + 자신이 보낸 요청이면 예외처리
        if(!user.getId().equals(receivedUser.getId()) && user.getId().equals(sendUser.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_FRIEND_REQUEST);
        }

        // 친구 요청이 대기상태가 아니면 예외처리
        if(!friendRequest.getInvitationStatus().equals(InvitationStatus.PENDING)) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        String message = "잘못된 요청입니다.";

        InvitationStatus status = requestDto.getInvitationStatus();

        //요청값이 수락일 때, 친구 데이터(2건) 추가
        if(status.equals(InvitationStatus.ACCEPTED)) {

            //친구 데이터 생성
            Friend friend1 = new Friend(sendUser, receivedUser);
            Friend friend2 = new Friend(receivedUser, sendUser);

            //친구 데이터 저장
            friendRepository.save(friend1);
            friendRepository.save(friend2);

            // 친구 요청 내역 삭제
            friendRequestRepository.delete(friendRequest);

            message = "친구로 등록되었습니다.";
        }

        // 거절이면 상태 변경
        if(status.equals(InvitationStatus.REJECTED)) {
            //친구요청상태 요청값으로 변경(수락 또는 거절)
            friendRequest.update(requestDto.getInvitationStatus());
            //친구요청상태 변경된내역 저장
            friendRequestRepository.save(friendRequest);

            message = "친구 요청을 거절했습니다.";
        }

        // 알림 보내기
        String notificationMessage = user.getNickName() + "님이 친구 요청을 " + status.getValue() + "했습니다.";
        sseEmitterService.send(sendUser, notificationMessage, NotificationType.FRIEND);
        fcmService.sendNotificationByToken("친구요청 수락", notificationMessage, "", sendUser);

        return message;
    }

    /**
     * 친구요청 내역 삭제 서비스 메서드 (ADMIN)
     */
    @Transactional
    public void deleteFriendRequest(FriendRequestDeleteRequestDto requestDto) {

        // 친구로 존재하는 지 확인
        if(!friendRequestRepository.isExistsFriendRequest(requestDto.getSendUserId(), requestDto.getReceivedUserId(),
                                             InvitationStatus.REJECTED)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        // 삭제
        friendRequestRepository.deleteBySendUserIdAndReceivedUserIdAndInvitationStatus(requestDto.getSendUserId(),
                                                                                        requestDto.getReceivedUserId(),
                                                                                        InvitationStatus.REJECTED);
    }

    /**
     *  친구 요청 취소 서비스 메서드
     * @param friendRequestId
     */
    public void cancelFriendRequest(Long friendRequestId, User user) {

        FriendRequest friendRequest = friendRequestRepository.findByIdOrElseThrow(friendRequestId);

        // 이미 거절된 요청이면 취소 안됨
        if(friendRequest.getInvitationStatus().equals(InvitationStatus.REJECTED)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 내가 보낸 요청이 맞는지 확인
        if(!friendRequest.getSendUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_FRIEND_REQUEST);
        }

        friendRequestRepository.delete(friendRequest);
    }
}
