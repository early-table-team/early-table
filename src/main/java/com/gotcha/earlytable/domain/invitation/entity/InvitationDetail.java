package com.gotcha.earlytable.domain.invitation.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "invitation_detail")
public class InvitationDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", nullable = false)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", nullable = false)
    private User receiveUser;

    private InvitationStatus invitationStatus;

    public InvitationDetail() {}

    public InvitationDetail(User sendUser, User receiveUser, InvitationStatus invitationStatus) {
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
        this.invitationStatus = invitationStatus;
    }

}
