package com.gotcha.earlytable.domain.party.dto;


import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.global.enums.PartyRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class PartyPeopleResponseDto {

    private String name;

    private String phoneNumber;

    private Long userId;

    private PartyRole partyRole;

    public PartyPeopleResponseDto(PartyPeople partyPeople) {
        this.name = partyPeople.getUser().getNickName();
        this.phoneNumber = partyPeople.getUser().getPhone();
        this.userId = partyPeople.getUser().getId();
        this.partyRole = partyPeople.getPartyRole();
    }

}
