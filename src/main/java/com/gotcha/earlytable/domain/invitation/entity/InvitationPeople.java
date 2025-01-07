package com.gotcha.earlytable.domain.invitation.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationRole;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "invitation_people")
public class InvitationPeople {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationPeopleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitation_id", nullable = false)
    private Invitation invitation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private InvitationRole invitationRole;

    public InvitationPeople() {}

}
