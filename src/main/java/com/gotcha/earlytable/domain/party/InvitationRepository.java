package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Invitation findBySendUserAndReceiveUserAndParty(User user, User receiveUser, Party party);

    List<Invitation> findByReceiveUser(User user);


    Invitation findByReceiveUserAndParty(User user, Party party);

    Optional<List<Invitation>> findByParty(Party party);

    default List<Invitation> findByPartyOrThrow(Party party){
        return  findByParty(party).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND));
    }

    Optional<Invitation> findByInvitationId(Long invitationId);

    default Invitation findByInvitationIdOrThrow(Long invitationId){
        return findByInvitationId(invitationId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND));
    }



}
