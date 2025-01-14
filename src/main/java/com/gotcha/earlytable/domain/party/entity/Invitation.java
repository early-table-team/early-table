package com.gotcha.earlytable.domain.party.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "invitation")
public class Invitation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", nullable = false)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", nullable = false)
    private User receiveUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;

    public Invitation() {}

    public Invitation(User sendUser, User receiveUser, Party party) {
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
        this.invitationStatus = InvitationStatus.PENDING;
        this.party = party;
    }

    public void changeStatus(InvitationStatus newStatus) {
        this.invitationStatus = newStatus;
    }

}
