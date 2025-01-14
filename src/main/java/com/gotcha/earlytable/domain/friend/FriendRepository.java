package com.gotcha.earlytable.domain.friend;

import com.gotcha.earlytable.domain.friend.entity.Friend;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    default Friend findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    List<Friend> findBySendUserId(Long id);

    boolean existsBySendUserIdAndReceivedUserId(Long sendUserId, Long receivedUserId);

    void deleteBySendUserIdAndReceivedUserId(Long sendUserId, Long receivedUserId);
}
