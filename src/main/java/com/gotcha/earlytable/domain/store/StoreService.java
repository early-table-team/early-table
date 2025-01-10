package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.file.FileRepository;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.menu.entity.QMenu;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.store.dto.StoreListResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreResponseDto;
import com.gotcha.earlytable.domain.store.dto.StoreSearchRequestDto;
import com.gotcha.earlytable.domain.store.entity.QStore;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public StoreService(StoreRepository storeRepository, UserRepository userRepository, FileRepository fileRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * 가게 생성 메서드 (Admin)
     *
     * @param requestDto
     * @return StoreResponseDto
     */
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto requestDto) {

        // 필요한 객체를 가져오기
        User user = userRepository.findByIdOrElseThrow(requestDto.getUserId());
        File file = fileRepository.findByIdOrElseThrow(requestDto.getFileId());

        // 가게 개수 제한 10개 이하 확인

        if (storeRepository.countStoreByUserId(requestDto.getUserId()) >= 10) {

            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }


        // 가게 객체 생성
        Store store = new Store(requestDto.getStoreName(), requestDto.getStoreTel(),
                requestDto.getStoreContents(), requestDto.getStoreAddress(),
                StoreStatus.PENDING, requestDto.getStoreCategory(),
                requestDto.getRegionTop(), requestDto.getRegionBottom(),
                user, file
        );

        // 가게 저장
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.toDto(savedStore);
    }

    /**
     * 가게 생성 메서드 from PendingStore
     *
     * @param pendingStore
     */
    @Transactional
    public void createStoreFromPendingStore(PendingStore pendingStore) {

        // 필요한 객체를 가져오기
        File file = fileRepository.findByIdOrElseThrow(pendingStore.getFileId());

        // 가게 개수 제한 10개 이하 확인
        if (storeRepository.countStoreByUserId(pendingStore.getUser().getId()) >= 10) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 가게 객체 생성
        Store store = new Store(pendingStore.getStoreName(), pendingStore.getStoreTel(),
                pendingStore.getStoreContents(), pendingStore.getStoreAddress(),
                StoreStatus.APPROVED, pendingStore.getStoreCategory(),
                pendingStore.getRegionTop(), pendingStore.getRegionBottom(),
                pendingStore.getUser(), file
        );

        // 가게 저장
        storeRepository.save(store);
    }

    /**
     * 가게 수정 메서드 (Admin)
     *
     * @param storeId
     * @param requestDto
     * @return StoreResponseDt
     */
    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreRequestDto requestDto) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 가게 내용 변경
        store.updateStore(requestDto);

        // 수정된 가게 정보 저장
        storeRepository.save(store);

        return StoreResponseDto.toDto(store);

    }

    /**
     * 가게 수정 메서드 from PendingStore
     *
     * @param pendingStore
     */
    @Transactional
    public void updateStoreFromPendingStore(PendingStore pendingStore) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(pendingStore.getStoreId());

        // 파일 정보 가져오기
        File file = fileRepository.findByIdOrElseThrow(pendingStore.getFileId());

        // 가게 내용 변경
        store.updateStoreFromPendingStore(pendingStore, file);

        // 수정된 가게 정보 저장
        storeRepository.save(store);
    }

    /**
     * 가게 단건 조회 메서드
     *
     * @param storeId
     * @return StoreResponseDto
     */
    public StoreResponseDto getStore(Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        return StoreResponseDto.toDto(store);
    }


    /**
     * 나의 가게 전체 조회 메서드
     *
     * @param userId
     * @return List<StoreListResponseDto>
     */
    public List<StoreListResponseDto> getStores(Long userId) {

        List<Store> storeList = storeRepository.findAllByUserId(userId);

        return storeList.stream().map(StoreListResponseDto::toDto).toList();
    }

    /**
     * 나의 가게 휴업 상태<-> 영업상태로 변경 메서드
     *
     * @param storeId
     * @param userId
     * @return String
     */
    @Transactional
    public String updateStoreStatus(Long storeId, Long userId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 나의 가게에 접근하는지 확인
        if (!store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        String message = "현 상태 유지되었습니다.";

        // 영업 상태로 변경
        if (store.getStoreStatus().equals(StoreStatus.RESTING)) {
            store.updateStoreStatus(StoreStatus.APPROVED);
            message = "정상 영업 상태로 변경되었습니다.";
        }

        // 휴업 상태로 변경
        if (store.getStoreStatus().equals(StoreStatus.APPROVED)) {
            store.updateStoreStatus(StoreStatus.RESTING);
            message = "휴업 상태로 변경되었습니다.";
        }

        storeRepository.save(store);

        return message;
    }

    /**
     * 가게 상태 변경 메서드 (Admin)
     *
     * @param storeId
     * @param storeStatus
     */
    @Transactional
    public void updateStoreStatus(Long storeId, StoreStatus storeStatus) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        store.updateStoreStatus(storeStatus);

        storeRepository.save(store);

    }

    /**
     * 가게 검색 조회
     *
     * @param requestDto
     * @return
     */
    @PersistenceContext
    private EntityManager entityManager;
    public List<StoreListResponseDto> searchStore(StoreSearchRequestDto requestDto) {

        QStore store = QStore.store;
        QMenu menu = QMenu.menu;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        List<Store> storeList = queryFactory.selectFrom(store)
                .leftJoin(store.menuList, menu).fetchJoin() // Store와 Menu 간의 조인
                .where(
                        searchWordContains(requestDto.getSearchWord(), store, menu), // 검색어 조건
                        regionTopEquals(requestDto.getRegionTop(), store),           // 상위 지역 조건
                        regionBottomEquals(requestDto.getRegionBottom(), store),     // 하위 지역 조건
                        storeCategoryEquals(requestDto.getStoreCategory(), store),   // 가게 카테고리 조건
                        maxPriceCondition(Math.toIntExact(requestDto.getMaxPrice()), menu),           // 최대 가격 조건
                        minPriceCondition(Math.toIntExact(requestDto.getMinPrice()), menu)            // 최소 가격 조건
                        // ToDo: 알러지 조건 추가
                )
                .distinct()
                .fetch();


        return storeList.stream()
                .map(StoreListResponseDto::toDto)
                .toList();
    }


    private BooleanExpression searchWordContains(String searchWord, QStore store, QMenu menu) {
        if (searchWord == null) {
            return null;
        }
        return store.storeName.containsIgnoreCase(searchWord)
                .or(menu.menuName.containsIgnoreCase(searchWord));
    }

    private BooleanExpression regionTopEquals(String regionTop, QStore store) {
        return regionTop == null ? null : store.RegionTop.equalsIgnoreCase(regionTop);
    }

    private BooleanExpression regionBottomEquals(String regionBottom, QStore store) {
        return regionBottom == null ? null : store.RegionBottom.equalsIgnoreCase(regionBottom);
    }

    private BooleanExpression storeCategoryEquals(StoreCategory storeCategory, QStore store) {
        return storeCategory == null ? null : store.storeCategory.eq(storeCategory);
    }

    private BooleanExpression maxPriceCondition(Integer maxPrice, QMenu menu) {
        if (maxPrice == null) {
            return null;
        }
        return menu.menuStatus.eq(MenuStatus.RECOMMENDED).and(menu.menuPrice.loe(maxPrice));
    }

    private BooleanExpression minPriceCondition(Integer minPrice, QMenu menu) {
        if (minPrice == null) {
            return null;
        }
        return menu.menuStatus.eq(MenuStatus.RECOMMENDED).and(menu.menuPrice.goe(minPrice));
    }

}
