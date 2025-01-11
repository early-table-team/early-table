package com.gotcha.earlytable.domain.party;

import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

    default Party findByPartyIdOrThrow(Long partyId){
        return findByPartyId(partyId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND));
    };

    Optional<Party> findByPartyId(Long partyId);

}
