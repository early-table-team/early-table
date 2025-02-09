package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyPeopleRepository extends JpaRepository<PartyPeople, Long> {

    Page<PartyPeople> findByUser(User user, Pageable pageable);

    void deleteByUser(User user);

    void deleteByUserId(Long userId);

    List<PartyPeople> findByParty(Party party);


    void deleteByUserAndPartyPartyId(User user, Long partyId);
}
