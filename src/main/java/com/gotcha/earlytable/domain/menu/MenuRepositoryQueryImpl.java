package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.MenuResponseDto;
import com.gotcha.earlytable.domain.menu.dto.MenuSearchRequestDto;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.gotcha.earlytable.domain.allergy.entity.QAllergy.allergy;
import static com.gotcha.earlytable.domain.menu.entity.QMenu.menu;

public class MenuRepositoryQueryImpl implements MenuRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public MenuRepositoryQueryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<MenuResponseDto> searchMenuQuery(Long storeId, MenuSearchRequestDto requestDto) {

        List<Menu> menuList = queryFactory.selectFrom(menu)
                .leftJoin(menu.allergyList, allergy).fetchJoin() // Menu와 allergy 간의 조인
                .where(
                        menu.store.storeId.eq(storeId),
                        searchWordContains(requestDto.getSearchWord()), // 검색어 조건
                        allergyCategoryExclude(requestDto.getAllergyCategory()), // 알러지 상위 조건
                        allergyStuffExclude(requestDto.getAllergyStuff()) // 알러지 하위 조건
                )
                .distinct()
                .fetch();


        return menuList.stream()
                .map(MenuResponseDto::toDto)
                .toList();
    }


    private BooleanExpression searchWordContains(String searchWord) {
        if (searchWord == null) {
            return null;
        }
        return menu.menuName.contains(searchWord);
    }

    private BooleanExpression allergyStuffExclude(List<String> allergyStuff) {
        if (allergyStuff == null || allergyStuff.isEmpty()) {
            return null;
        }

        BooleanExpression condition = null;
        for (String allergyKey : allergyStuff) {
            BooleanExpression currentCondition = menu.allergyList.any().allergyStuff
                    .allergyStuff.contains(allergyKey).not();
                condition = (condition == null) ? currentCondition : condition.or(currentCondition);

        }

        return condition;
    }

    private BooleanExpression allergyCategoryExclude(List<String> allergyCategory) {
        if (allergyCategory == null || allergyCategory.isEmpty()) {
            return null;
        }

        BooleanExpression condition = null;
        for (String allergyKey : allergyCategory) {
            BooleanExpression currentCondition = menu.allergyList.any().allergyStuff.allergyCategory
                    .allergyCategory.contains(allergyKey).not();
                condition = (condition == null) ? currentCondition : condition.or(currentCondition);

        }

        return condition;
    }
}
