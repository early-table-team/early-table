package com.gotcha.earlytable.domain.Invitation.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "invitation_detail")
public class InvitationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", nullable = false)
    private User sendUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", nullable = false)
    private User receiveUserId;

    private InvitationStatus invitationStatus;

    public InvitationDetail() {}

}
