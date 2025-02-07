package com.gotcha.earlytable.domain.user.dto;

import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserRegisterRequestDto {

    @NotBlank
    private final String nickname;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "올바른 이메일 형식이 아닙니다.")
    private final String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$",
            message = "비밀번호는 영문/숫자/특수문자를 포함해야 합니다.")
    private final String password;

    @NotBlank
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "유효한 핸드폰 번호를 입력하세요.")
    private final String phone;

    @NotNull
    private final Auth auth;

    private final MultipartFile profileImage;

    public UserRegisterRequestDto(String nickname, String email, String password, String phone, Auth auth, MultipartFile profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.auth = auth;
        this.profileImage = profileImage;
    }
}
