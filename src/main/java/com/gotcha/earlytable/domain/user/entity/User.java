package com.gotcha.earlytable.domain.user.entity;

import com.gotcha.earlytable.domain.store.entity.InterestStore;
import com.gotcha.earlytable.domain.user.dto.UserRegisterRequestDto;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private String nickName; // 닉네임

    @Column(nullable = false)
    private String password; //비밀번호

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Auth auth; // 유저 권한

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; //유저 상태

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InterestStore> interestStoreList = new ArrayList<>();

    public User(String email, String nickName, String password, String phone, Auth auth, Status status) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.phone = phone;
        this.auth = auth;
        this.status = status;
    }

    public User() {

    }

    public static User toEntity(UserRegisterRequestDto requestDto, String EncodingPassword){
        return new User(
                requestDto.getEmail(),
                requestDto.getNickname(),
                EncodingPassword,
                requestDto.getPhoneNumber(),
                requestDto.getAuth(),
                Status.NORMAL
        );
    }
}
