package com.gotcha.earlytable.domain.Friend.entity;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "friend_request")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", nullable = false)
    private User sendUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_user_id", nullable = false)
    private User receivedUserId;

    private InvitationStatus invitationStatus;
}
