package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.store.dto.StoreSearchRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchResponseDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.gotcha.earlytable.domain.menu.entity.QMenu.menu;
import static com.gotcha.earlytable.domain.store.entity.QStore.store;

public class StoreRepositoryQueryImpl implements StoreRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryQueryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<StoreSearchResponseDto> searchStoreQuery(StoreSearchRequestDto requestDto, Pageable pageable) {

        List<Store> storeList = queryFactory.selectFrom(store)
                .leftJoin(store.menuList, menu).fetchJoin() // Store와 Menu 간의 조인
                .where(
                        searchWordContains(requestDto.getSearchWord()), // 검색어 조건
                        regionTopEquals(requestDto.getRegionTop()),           // 상위 지역 조건
                        regionBottomEquals(requestDto.getRegionBottom()),     // 하위 지역 조건
                        storeCategoryEquals(requestDto.getStoreCategory()),   // 가게 카테고리 조건
                        maxPriceCondition(requestDto.getMaxPrice()),           // 최대 가격 조건
                        minPriceCondition(requestDto.getMinPrice()),            // 최소 가격 조건
                        allergyCategoryExclude(requestDto.getAllergyCategory()), // 알러지 상위 조건
                        allergyStuffExclude(requestDto.getAllergyStuff()) // 알러지 하위 조건
                )
                .distinct()
                .offset(pageable.getOffset()) // 페이지 시작점
                .limit(pageable.getPageSize()) // 페이지 크기 설정
                .fetch();


        return storeList.stream()
                .map(StoreSearchResponseDto::toDto)
                .toList();
    }


    private BooleanExpression searchWordContains(String searchWord) {
        if (searchWord == null) {
            return null;
        }
        return store.storeName.contains(searchWord)
                .or(menu.menuName.contains(searchWord));
    }

    private BooleanExpression regionTopEquals(String regionTop) {
        return regionTop == null ? null : store.regionTop.stringValue().equalsIgnoreCase(regionTop);
    }

    private BooleanExpression regionBottomEquals(String regionBottom) {
        return regionBottom == null ? null : store.regionBottom.stringValue().equalsIgnoreCase(regionBottom);
    }

    private BooleanExpression storeCategoryEquals(String storeCategory) {
        return storeCategory == null ? null : store.storeCategory.stringValue().eq(storeCategory);
    }

    private BooleanExpression maxPriceCondition(Long maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return menu.menuStatus.eq(MenuStatus.RECOMMENDED).and(menu.menuPrice.loe(maxPrice));
    }

    private BooleanExpression minPriceCondition(Long minPrice) {
        if (minPrice == null) {
            return null;
        }
        return menu.menuStatus.eq(MenuStatus.RECOMMENDED).and(menu.menuPrice.goe(minPrice));
    }

    private BooleanExpression allergyStuffExclude(List<String> allergyStuff) {
        if (allergyStuff == null || allergyStuff.isEmpty()) {
            return null;
        }

        BooleanExpression condition = null;
        for (String allergyKey : allergyStuff) {
            BooleanExpression currentCondition = store.menuList.any().allergyList.any().allergyStuff
                    .allergyStuff.contains(allergyKey).not();
            condition = (condition == null) ? currentCondition : condition.and(currentCondition);

        }

        return condition;
    }

    private BooleanExpression allergyCategoryExclude(List<String> allergyCategory) {
        if (allergyCategory == null || allergyCategory.isEmpty()) {
            return null;
        }

        BooleanExpression condition = null;
        for (String allergyKey : allergyCategory) {
            BooleanExpression currentCondition = store.menuList.any().allergyList.any().allergyStuff.allergyCategory
                    .allergyCategory.contains(allergyKey).not();
            condition = (condition == null) ? currentCondition : condition.and(currentCondition);

        }

        return condition;
    }
}
