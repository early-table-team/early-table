package com.gotcha.earlytable.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

//@Getter
//public class UserSearchRequestDto {
//    private String nickname;
//    private String email;
//
//    @Pattern(regexp = "\\d{4}", message = "phoneNumberBottom은 반드시 네 자리 숫자여야 합니다.")
//    private String phoneNumberBottom;
//
//    public UserSearchRequestDto(String nickname, String email, String phoneNumberBottom) {
//        this.nickname = nickname;
//        this.email = email;
//        this.phoneNumberBottom = phoneNumberBottom;
//    }
//}

@Getter
public class UserSearchRequestDto {
    private String searchKeyword;  // 단일 검색어 필드

    public UserSearchRequestDto(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }
}
