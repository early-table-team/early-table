package com.gotcha.earlytable.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPWRequestDto {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$",
            message = "비밀번호는 영문/숫자/특수문자를 포함해야 합니다.")
    private final String password;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$",
            message = "비밀번호는 영문/숫자/특수문자를 포함해야 합니다.")
    private final String newPassword;

    public UserPWRequestDto(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }
}
