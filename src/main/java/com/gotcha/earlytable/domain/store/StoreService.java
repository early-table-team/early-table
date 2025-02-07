package com.gotcha.earlytable.domain.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotcha.earlytable.domain.allergy.AllergyCategoryRepository;
import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileRepository;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.keyword.StoreKeywordRepository;
import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.reservation.ReservationRepository;
import com.gotcha.earlytable.domain.store.dto.*;
import com.gotcha.earlytable.domain.store.entity.*;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.entity.StoreTable;
import com.gotcha.earlytable.domain.store.entity.StoreTimeSlot;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.store.storeHour.StoreHourRepository;
import com.gotcha.earlytable.domain.store.storeRest.StoreRestRepository;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileDetailService fileDetailService;
    private final AllergyCategoryRepository allergyCategoryRepository;
    private final ReservationRepository reservationRepository;
    private final StoreKeywordRepository storeKeywordRepository;
    private final StoreRestRepository storeRestRepository;
    private final StoreHourRepository storeHourRepository;
    private final RedissonClient redissonClient;


    public StoreService(StoreRepository storeRepository, UserRepository userRepository,
                        FileRepository fileRepository, FileDetailService fileDetailService,
                        AllergyCategoryRepository allergyCategoryRepository, ReservationRepository reservationRepository,
                        StoreKeywordRepository storeKeywordRepository, StoreRestRepository storeRestRepository,
                        StoreHourRepository storeHourRepository, RedissonClient redissonClient) {

        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileDetailService = fileDetailService;
        this.allergyCategoryRepository = allergyCategoryRepository;
        this.reservationRepository = reservationRepository;
        this.storeKeywordRepository = storeKeywordRepository;
        this.storeRestRepository = storeRestRepository;
        this.storeHourRepository = storeHourRepository;
        this.redissonClient = redissonClient;
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

        // 가게 개수 제한 10개 이하 확인
        if (storeRepository.countStoreByUserId(requestDto.getUserId()) >= 10) {

            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        File file = fileRepository.save(new File());

        // 이미지 파일들 저장
        if(requestDto.getStoreImageList() != null && !requestDto.getStoreImageList().get(0).isEmpty()) {
            // 프로필 이미지 파일 저장
            fileDetailService.createImageFiles(requestDto.getStoreImageList(), file);
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
     * @return StoreResponseDto
     */
    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreUpdateRequestDto requestDto) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 가게 내용 변경 및 저장
        store.updateStore(requestDto);
        storeRepository.save(store);

        // 이미지 수정
        if(requestDto.getFileUrlList() != null && !requestDto.getFileUrlList().isEmpty()) {

            fileDetailService.updateFileDetail(requestDto.getNewStoreImageList(), requestDto.getFileUrlList(), store.getFile());
        }

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

        // 이미지가 변경되었는지 확인
        if(!store.getFile().getFileId().equals(pendingStore.getFileId())) {
            // 파일 삭제
            fileRepository.deleteById(store.getFile().getFileId());
        }

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

        String message = "현재 상태를 변경할 수 없습니다. 관리자에게 문의하세요.";

        // 영업 상태로 변경
        if (store.getStoreStatus().equals(StoreStatus.RESTING)) {
            store.updateStoreStatus(StoreStatus.APPROVED);
            message = "정상 영업 상태로 변경되었습니다.";
        }
        // 휴업 상태로 변경
        else if (store.getStoreStatus().equals(StoreStatus.APPROVED)) {
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


    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper

    /**
     * 가게 조건 검색 메서드
     *
     * @param requestDto
     * @return
     */
    public List<StoreSearchResponseDto> searchStore(StoreSearchRequestDto requestDto) {
        String cacheKey = "store_search:" + getCacheKey(requestDto);

        // RBucket을 JsonJacksonCodec과 함께 사용
        RBucket<String> cachedResult = redissonClient.getBucket(cacheKey, JsonJacksonCodec.INSTANCE);

        Instant start = Instant.now(); // 시작 시간 기록

        // 캐시된 결과가 있으면 반환
        String resultJson = cachedResult.get();
        List<StoreSearchResponseDto> result = null;
        if (resultJson != null && !resultJson.isEmpty()) {
            try {
                result = objectMapper.readValue(resultJson, objectMapper.getTypeFactory().constructCollectionType(List.class, StoreSearchResponseDto.class));
            } catch (Exception e) {
                log.error("Error deserializing cached result", e);
            }
        }

        if (result == null || result.isEmpty()) {
            // 캐시된 결과가 없으면 DB에서 조회 후 캐시에 저장
            result = storeRepository.searchStoreQuery(requestDto);

            try {
                String resultJsonToCache = objectMapper.writeValueAsString(result); // List to JSON
                cachedResult.set(resultJsonToCache, 10, TimeUnit.MINUTES); // 캐시 만료 시간은 10분으로 설정
            } catch (Exception e) {
                log.error("Error serializing result to cache", e);
            }
        }

        Instant end = Instant.now(); // 종료 시간 기록
        long elapsedTime = Duration.between(start, end).toMillis(); // 실행 시간(ms)

        log.info("searchStoreQuery 실행 시간: {} ms, 결과 개수: {}", elapsedTime, result.size());

        return result;
    }

    // 캐시 키를 위한 핵심 파라미터들만 조합하는 메소드
    private String getCacheKey(StoreSearchRequestDto requestDto) {
        return requestDto.getSearchWord() + ":" +
                requestDto.getRegionTop() + ":" +
                requestDto.getRegionBottom() + ":" +
                requestDto.getStoreCategory(); // 예시: 중요한 파라미터만 조합
    }

    /**
     * 필터 목록 조회 메서드
     *
     * @return FiltersResponseDto
     */
    public FiltersResponseDto getFilters() {

        Map<String, List<String>> regionMap = new HashMap<>();

        // RegionTop 별로 RegionBottom을 그룹화
        for (RegionTop regionTop : RegionTop.values()) {
            List<String> regionBottomList = new ArrayList<>();

            // RegionBottom enum을 순회하면서 현재 RegionTop에 맞는 값들을 추가
            for (RegionBottom regionBottom : RegionBottom.values()) {
                if (regionBottom.getRegionTop() == regionTop) {
                    regionBottomList.add(regionBottom.getName());
                }
            }

            // Map에 RegionTop을 키로 하고 해당하는 RegionBottom 리스트를 값으로 저장
            regionMap.put(regionTop.getName(), regionBottomList);
        }

        // 카테고리 리스트 생성
        List<String> categoryList = new ArrayList<>();

        for (StoreCategory category : StoreCategory.values()) {
            categoryList.add(category.getCategoryName());
        }


        List<Object[]> results = allergyCategoryRepository.findAllCategoryWithStuff();

        Map<String, List<String>> allergyMap = new HashMap<>();

        for (Object[] row : results) {
            String categoryName = (String) row[1]; // 대분류 이름
            String stuffName = (String) row[3];   // 소분류 이름

            allergyMap.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(stuffName);
        }




        return new FiltersResponseDto(regionMap, categoryList, allergyMap);
    }

    /**
     * 가게 모든 타임, 모든 테이블의 잔여 개수 가져오기 메서드
     *
     * @param storeId
     * @param date
     * @return
     */
    public List<StoreReservationTotalDto> getStoreReservationTotal(Long storeId, LocalDate date, Integer personnelCount) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);
        // 현재 날짜
        LocalDate today = LocalDate.now();

        // 현재 시간
        LocalTime now = LocalTime.now();

        List<StoreReservationTotalDto> totalDtoList = new ArrayList<>();

        // 모든 시간에
        for (StoreTimeSlot storeTimeSlot : store.getStoreTimeSlotList()) {

            // 오늘 날짜일 때만 현재 시간보다 이전 예약 시간 제외
            if (date.equals(today) && storeTimeSlot.getReservationTime().isBefore(now)) {
                continue;
            }

            // 모든 테이블에
            for (StoreTable storeTable : store.getStoreTableList()) {
                // 예약 타임
                LocalTime reservationTime = storeTimeSlot.getReservationTime();
                // 최대 수용 가능 인원
                int tableMaxNumber = storeTable.getTableMaxNumber();
                // 최소 수용 인원
                int tableMinNumber = tableMaxNumber - 1;
                // 잔여 개수
                int remainTableCount = storeTable.getTableCount() - reservationRepository
                        .countByReservationDateAndReservationTimeAndTableSizeAndReservationStatusNot(
                                date, reservationTime, tableMaxNumber, ReservationStatus.CANCELED);

                // personnelCount 값이 지정되지 않았으면 (null인 경우)
                if (personnelCount == null || (personnelCount >= tableMinNumber && personnelCount <= tableMaxNumber)) {
                    totalDtoList.add(new StoreReservationTotalDto(reservationTime, tableMaxNumber, tableMinNumber, remainTableCount));
                }
            }
        }

        return totalDtoList;
    }

    /**
     *  키워드로 가게 검색
     * @param keyword
     * @return
     */
    public List<StoreSearchResponseDto> searchKeywordStore(String keyword) {

        // 스토어 키워드가 일치하는 것들을 리스트 타입으로 가져옴
        List<StoreKeyword> storeKeyword = storeKeywordRepository.findAllByKeywordKeyword(keyword);


        // 해당 가게들로 DTO를 생성 후 반환
        List<StoreSearchResponseDto> dtos = new ArrayList<>();
        for(StoreKeyword storeKeywordDto : storeKeyword) {
            StoreSearchResponseDto dto = new StoreSearchResponseDto(storeKeywordDto.getStore());
            dtos.add(dto);
        }

        return dtos;

    }

    public List<StoreListResponseDto> getWaitingAbleStores(Long id) {
        List<StoreListResponseDto> responseDtos = new ArrayList<>();
        List<Store> storeList = storeRepository.findAllByUserId(id);

        return storeList.stream().filter(store -> store.getStoreReservationTypeList().stream()
                .anyMatch(storeReservationType -> storeReservationType.getReservationType().equals(ReservationType.ONSITE)))
                .map(StoreListResponseDto::toDto).toList();

    }

    /**
     * 가게 휴일 조회
     *
     * @param storeId
     * @return StoreRestDateResponseDto
     */
    public StoreRestDateResponseDto getRestDate(Long storeId) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);


        List<String> restDateList = new ArrayList<>();
        List<Integer> restWeekdayList = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6)); // 초기화

        // 일정 구간으로 휴무일 조회하기
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);

        List<StoreRest> storeRestList = storeRestRepository
                .findAllByStoreStoreIdAndStoreOffDayBetween(storeId, startDate, endDate);

        for (StoreRest storeRest : storeRestList) {
            restDateList.add(storeRest.getStoreOffDay().toString());
        }

        List<StoreHour> storeHourList = storeHourRepository.findByStore(store);
        for (StoreHour storeHour : storeHourList) {
            switch (storeHour.getDayOfWeek()) {
                case SUN -> restWeekdayList.remove(Integer.valueOf(0)); // 0 제거
                case MON -> restWeekdayList.remove(Integer.valueOf(1)); // 1 제거
                case TUE -> restWeekdayList.remove(Integer.valueOf(2)); // 2 제거
                case WED -> restWeekdayList.remove(Integer.valueOf(3)); // 3 제거
                case THU -> restWeekdayList.remove(Integer.valueOf(4)); // 4 제거
                case FRI -> restWeekdayList.remove(Integer.valueOf(5)); // 5 제거
                case SAT -> restWeekdayList.remove(Integer.valueOf(6)); // 6 제거
            }
        }

        return new StoreRestDateResponseDto(restDateList, restWeekdayList);
    }
}
