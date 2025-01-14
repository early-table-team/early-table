package com.gotcha.earlytable.domain.party.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.PartyRole;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "party_people")
public class PartyPeople extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyPeopleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private PartyRole partyRole;

    public PartyPeople() {
    }

    public PartyPeople(Party party, User user, PartyRole partyRole) {
        addParty(party);
        this.user = user;
        this.partyRole = partyRole;
    }

    private void addParty(Party party) {
        this.party = party;
        party.getPartyPeople().add(this);
    }
}
