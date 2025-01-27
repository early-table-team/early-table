package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.store.dto.StoreSearchRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchResponseDto;
import com.gotcha.earlytable.domain.user.dto.OtherUserResponseDto;
import com.gotcha.earlytable.domain.user.dto.UserSearchRequestDto;
import com.gotcha.earlytable.domain.user.dto.UserSearchResponseDto;

import java.util.List;

public interface UserRepositoryQuery {

    List<UserSearchResponseDto> searchUserQuery(UserSearchRequestDto requestDto);
}
