package com.gotcha.earlytable.domain.party.dto;


import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import lombok.Getter;

@Getter
public class PartyPeopleResponseDto {

    private String name;

    private String phoneNumber;

    private Long userId;

    public PartyPeopleResponseDto(PartyPeople partyPeople) {
        this.name = partyPeople.getUser().getNickName();
        this.phoneNumber = partyPeople.getUser().getPhone();
        this.userId = partyPeople.getUser().getId();
    }

}
