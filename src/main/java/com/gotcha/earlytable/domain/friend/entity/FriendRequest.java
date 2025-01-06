package com.gotcha.earlytable.domain.friend.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "friend_request")
public class FriendRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", nullable = false)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_user_id", nullable = false)
    private User receivedUser;

    private InvitationStatus invitationStatus;
}
