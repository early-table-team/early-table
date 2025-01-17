package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.MenuResponseDto;
import com.gotcha.earlytable.domain.menu.dto.MenuSearchRequestDto;

import java.util.List;

public interface MenuRepositoryQuery {

    List<MenuResponseDto> searchMenuQuery(Long storeId, MenuSearchRequestDto requestDto);
}
