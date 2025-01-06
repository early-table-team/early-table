package com.gotcha.earlytable.domain.user.entity;

import com.gotcha.earlytable.domain.user.dto.UserRegisterRequestDto;
import com.gotcha.earlytable.global.base.BaseEntity;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email; // 이메일

    @NotNull
    private String nickName; // 닉네임

    @NotNull
    private String password; //비밀번호

    @NotNull
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Auth auth; // 유저 권한

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status; //유저 상태

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
