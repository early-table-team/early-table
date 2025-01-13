package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.dto.FriendRequestRequestDto;
import com.gotcha.earlytable.domain.friend.dto.FriendRequestResponseDto;
import com.gotcha.earlytable.domain.friend.entity.Friend;
import com.gotcha.earlytable.domain.friend.entity.FriendRequest;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    /**
     * 친구 요청 보내기 서비스 메서드
     */
    @Transactional
    public FriendRequestResponseDto createFriendRequest(User user, FriendRequestRequestDto friendRequestRequestDto) {
        //친구의 user 정보 찾기
        User receivedUser = userRepository.findByIdOrElseThrow(friendRequestRequestDto.getReceivedUserId());

        //이미 친구관계일 때 예외처리
        if(friendRepository.existsBySendUserIdAndReceivedUserId(user.getId(), friendRequestRequestDto.getReceivedUserId())) {
            throw new ConflictException(ErrorCode.ALREADY_IN_FRIEND);
        }

        //상대가 보낸 요청이 이미 존재할 때 -> 존재하는 요청 건 수락처리, 신규 요청은 생성하지 않음
        if(friendRequestRepository.existsBySendUserIdAndReceivedUserIdAndInvitationStatus(receivedUser.getId(), user.getId(), InvitationStatus.PENDING)) {
            //이미 존재하는 요청건 찾아오기
            FriendRequest friendRequest = friendRequestRepository.findBySendUserIdAndReceivedUserIdAndInvitationStatus(receivedUser.getId(), user.getId(), InvitationStatus.PENDING);

            //상대가 보낸 요청 수락(->친구등록) 처리
            FriendRequestRequestDto reverseFriendRequestRequestDto = new FriendRequestRequestDto(receivedUser.getId(), user.getId(), InvitationStatus.ACCEPTED);
            return this.updateFriendRequestStatus(friendRequest.getFriendRequestId(), reverseFriendRequestRequestDto);
        }

        //내가 보낸 대기상태인 요청 건 존재시 예외처리
        if (friendRequestRepository.existsBySendUserIdAndReceivedUserIdAndInvitationStatus(user.getId(), receivedUser.getId(), InvitationStatus.PENDING)) {
            throw new ConflictException(ErrorCode.ALREADY_REQUESTED);
        }

        //거절상태인 요청 건 5건 이상 존재할 때 예외처리
        if(friendRequestRepository.countBySendUserIdAndReceivedUserIdAndInvitationStatus(user.getId(), receivedUser.getId(), InvitationStatus.REJECTED) >= 5) {
            throw new BadRequestException(ErrorCode.NO_MORE_REQUEST_AVAILABLE);
        }

        //친구 요청 등록
        FriendRequest friendRequest = new FriendRequest(user,receivedUser, InvitationStatus.PENDING);

        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        return FriendRequestResponseDto.toDto(savedFriendRequest);
    }

    /**
     * 친구 요청 수신 목록 조회 서비스 메서드
     */
    public List<FriendRequestResponseDto> getFriendRequestList(User user) {
        List<FriendRequest> friendRequestResponseDtoList = friendRequestRepository.findByReceivedUserIdAndInvitationStatus(user.getId(), InvitationStatus.PENDING);

        return friendRequestResponseDtoList.stream().map(FriendRequestResponseDto::toDto).toList();
    }

    /**
     * 친구 요청 상태(수락/거절) 변경 서비스 메서드
     */
    @Transactional
    public FriendRequestResponseDto updateFriendRequestStatus(Long friendRequestId, FriendRequestRequestDto friendRequestRequestDto) {
        FriendRequest friendRequest = friendRequestRepository.findByIdOrElseThrow(friendRequestId);

        //친구 요청 수락으로 변경 시, 친구 데이터(2건) 추가
        if(friendRequestRequestDto.getInvitationStatus().equals(InvitationStatus.ACCEPTED)) {
            //친구 유저 데이터 받아오기
            User receivedUser = userRepository.findByIdOrElseThrow(friendRequestRequestDto.getReceivedUserId());
            User sendUser = userRepository.findByIdOrElseThrow(friendRequestRequestDto.getSendUserId());

            //친구 데이터 생성
            Friend friend1 = new Friend(sendUser, receivedUser);
            Friend friend2 = new Friend(receivedUser, sendUser);

            //친구 데이터 저장
            friendRepository.save(friend1);
            friendRepository.save(friend2);
        }

        //친구 요청 상태 요청값으로 변경
        friendRequest.update(friendRequestRequestDto.getInvitationStatus());

        //친구 요청 상태 변경된 내역 저장
        FriendRequest updatedFriendRequest = friendRequestRepository.save(friendRequest);

        return FriendRequestResponseDto.toDto(updatedFriendRequest);
    }

    /**
     * 친구요청 내역 삭제 서비스 메서드 (ADMIN)
     */
    @Transactional
    public void deleteFriendRequest(FriendRequestRequestDto friendRequestRequestDto) {
        if(!friendRequestRepository.existsBySendUserIdAndReceivedUserIdAndInvitationStatus(friendRequestRequestDto.getSendUserId(), friendRequestRequestDto.getReceivedUserId(), InvitationStatus.REJECTED)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        friendRequestRepository.deleteBySendUserIdAndReceivedUserIdAndInvitationStatus(friendRequestRequestDto.getSendUserId(), friendRequestRequestDto.getReceivedUserId(), InvitationStatus.REJECTED);
    }
}
