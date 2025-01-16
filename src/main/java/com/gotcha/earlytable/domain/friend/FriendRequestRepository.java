package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.entity.FriendRequest;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository <FriendRequest, Long> {

    default FriendRequest findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    int countBySendUserIdAndReceivedUserIdAndInvitationStatus(Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus);

    List<FriendRequest> findByReceivedUserIdAndInvitationStatus(Long id, InvitationStatus invitationStatus);


    default boolean isExistsFriendRequest(Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus) {
        return existsBySendUserIdAndReceivedUserIdAndInvitationStatus(sendUserId, receivedUserId, invitationStatus);
    }

    boolean existsBySendUserIdAndReceivedUserIdAndInvitationStatus(Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus);

    void deleteBySendUserIdAndReceivedUserIdAndInvitationStatus(Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus);

    FriendRequest findBySendUserIdAndReceivedUserIdAndInvitationStatus(Long sendUserId, Long receivedUserId, InvitationStatus invitationStatus);
}
