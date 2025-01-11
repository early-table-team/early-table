package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.entity.Invitation;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Invitation findBySendUserAndReceiveUserAndParty(User user, User receiveUser, Party party);

    List<Invitation> findByReceiveUser(User user);


    Invitation findByReceiveUserAndParty(User user, Party party);

    Optional<Object> findByParty(Party party);

}
