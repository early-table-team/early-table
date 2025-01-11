package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.store.dto.StoreListResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.gotcha.earlytable.domain.allergy.entity.QAllergy.allergy;
import static com.gotcha.earlytable.domain.menu.entity.QMenu.menu;
import static com.gotcha.earlytable.domain.store.entity.QStore.store;

public class StoreRepositoryQueryImpl implements StoreRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryQueryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<StoreListResponseDto> searchStoreQuery(StoreSearchRequestDto requestDto) {

        List<Store> storeList = queryFactory.selectFrom(store)
                .leftJoin(store.menuList, menu).fetchJoin() // Store와 Menu 간의 조인
                .leftJoin(menu.allergyList, allergy).fetchJoin()
                .where(
                        searchWordContains(requestDto.getSearchWord()), // 검색어 조건
                        regionTopEquals(requestDto.getRegionTop()),           // 상위 지역 조건
                        regionBottomEquals(requestDto.getRegionBottom()),     // 하위 지역 조건
                        storeCategoryEquals(requestDto.getStoreCategory()),   // 가게 카테고리 조건
                        maxPriceCondition(Math.toIntExact(requestDto.getMaxPrice())),           // 최대 가격 조건
                        minPriceCondition(Math.toIntExact(requestDto.getMinPrice())),            // 최소 가격 조건
                        allergyCategoryExclude(requestDto.getAllergyCategory()), // 알러지 상위 조건
                        allergyStuffExclude(requestDto.getAllergyStuff()) // 알러지 하위 조건
                )
                .distinct()
                .fetch();


        return storeList.stream()
                .map(StoreListResponseDto::toDto)
                .toList();
    }


    private BooleanExpression searchWordContains(String searchWord) {
        if (searchWord == null) {
            return null;
        }
        return store.storeName.containsIgnoreCase(searchWord)
                .or(menu.menuName.containsIgnoreCase(searchWord));
    }

    private BooleanExpression regionTopEquals(String regionTop) {
        return regionTop == null ? null : store.RegionTop.equalsIgnoreCase(regionTop);
    }

    private BooleanExpression regionBottomEquals(String regionBottom) {
        return regionBottom == null ? null : store.RegionBottom.equalsIgnoreCase(regionBottom);
    }

    private BooleanExpression storeCategoryEquals(StoreCategory storeCategory) {
        return storeCategory == null ? null : store.storeCategory.eq(storeCategory);
    }

    private BooleanExpression maxPriceCondition(Integer maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return menu.menuStatus.eq(MenuStatus.RECOMMENDED).and(menu.menuPrice.loe(maxPrice));
    }

    private BooleanExpression minPriceCondition(Integer minPrice) {
        if (minPrice == null) {
            return null;
        }
        return menu.menuStatus.eq(MenuStatus.RECOMMENDED).and(menu.menuPrice.goe(minPrice));
    }

    private BooleanExpression allergyStuffExclude(List<String> allergyStuff) {
        if (allergyStuff == null) {
            return null;
        }

        BooleanExpression condition = null;
        for (String allergyKey : allergyStuff) {
            condition = menu.allergyList.any().allergyStuff.allergyCategory.allergyCategory.contains(allergyKey)
                    .not();
        }

        return condition;
    }

    private BooleanExpression allergyCategoryExclude(List<String> allergyCategory) {
        if (allergyCategory == null) {
            return null;
        }

        BooleanExpression condition = null;
        for (String allergyKey : allergyCategory) {
            condition = menu.allergyList.any().allergyStuff.allergyStuff.contains(allergyKey)
                    .not();
        }

        return condition;
    }
}
