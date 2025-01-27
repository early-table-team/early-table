package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.user.dto.UserSearchRequestDto;
import com.gotcha.earlytable.domain.user.dto.UserSearchResponseDto;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Status;
import com.gotcha.earlytable.global.enums.Auth;  // Auth enum import 추가
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.gotcha.earlytable.domain.user.entity.QUser.user;

public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryQueryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<UserSearchResponseDto> searchUserQuery(UserSearchRequestDto requestDto) {
        List<User> userList = queryFactory.selectFrom(user)
                .where(nicknameContains(requestDto.getNickname()),
                        emailContains(requestDto.getEmail()),
                        phoneEndsWith(requestDto.getPhoneNumberBottom()),
                        isNotDeleted(),  // DELETE 상태인 유저를 제외하는 조건 추가
                        isAuthUser()  // auth가 USER인 유저만 필터링하는 조건 추가
                )
                .distinct()
                .fetch();

        return userList.stream()
                .map(UserSearchResponseDto::toDto)
                .toList();
    }

    private BooleanExpression nicknameContains(String nickname) {
        if ((nickname == null)||(nickname.isEmpty())) {
            return null;
        }
        return user.nickName.contains(nickname);
    }

    private BooleanExpression emailContains(String email) {
        if ((email == null)||(email.isEmpty())) {
            return null;
        }
        return user.email.contains(email);
    }

    private BooleanExpression phoneEndsWith(String phoneNumberBottom) {
        if ((phoneNumberBottom == null)||(phoneNumberBottom.isEmpty())) {
            return null;
        }
        return user.phone.endsWith(phoneNumberBottom);
    }
    // DELETE 상태인 유저를 제외하는 조건
    private BooleanExpression isNotDeleted() {
        return user.status.ne(Status.DELETE);  // DELETE 상태인 유저를 제외
    }

    // auth가 USER인 유저만 필터링하는 조건
    private BooleanExpression isAuthUser() {
        return user.auth.eq(Auth.USER);  // auth가 USER인 유저만 필터링
    }

    // 유틸리티 메서드: null 또는 빈 문자열 체크
    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
