package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.party.PartyPeopleRepository;
import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.reservation.dto.*;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.reservation.entity.ReservationMenu;
import com.gotcha.earlytable.domain.review.ReviewRepository;
import com.gotcha.earlytable.domain.review.enums.ReviewTarget;
import com.gotcha.earlytable.domain.store.*;
import com.gotcha.earlytable.domain.store.entity.*;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final PartyRepository partyRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final PartyPeopleRepository partyPeopleRepository;
    private final RedissonClient redissonClient;
    private final TransactionTemplate transactionTemplate;
    private final ReviewRepository reviewRepository;

    public ReservationService(ReservationRepository reservationRepository, StoreRepository storeRepository,
                              MenuRepository menuRepository, PartyRepository partyRepository,
                              ReservationMenuRepository reservationMenuRepository,
                              PartyPeopleRepository partyPeopleRepository, RedissonClient redissonClient, TransactionTemplate transactionTemplate, ReviewRepository reviewRepository) {
        this.reservationRepository = reservationRepository;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.partyRepository = partyRepository;
        this.reservationMenuRepository = reservationMenuRepository;
        this.partyPeopleRepository = partyPeopleRepository;
        this.redissonClient = redissonClient;
        this.transactionTemplate = transactionTemplate;
        this.reviewRepository = reviewRepository;
    }

    /**
     * 예약잡기 메서드
     *
     * @param storeId
     * @param requestDto
     * @param user
     * @return ReservationCreateResponseDto
     */
    @Transactional
    public ReservationCreateResponseDto createReservation(Long storeId, ReservationCreateRequestDto requestDto, User user) {
        // TOdo : 해당 가게가 존재하는가?
        Store store = storeRepository.findByIdWithLock(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // TODO : 가게 예약 타입이 예약이 맞는가?
        boolean dontReservation = store.getStoreReservationTypeList().stream()
                .noneMatch(storeReservationType -> storeReservationType.getReservationType() == ReservationType.RESERVATION);

        if (dontReservation) {
            throw new CustomException(ErrorCode.UNAVAILABLE_RESERVATION_TYPE);
        }

        // TODO : 자릿수 맥스값보다 많은 인원이 신청한경우
        Integer maxSeat = store.getStoreTableList().stream()
                .map(StoreTable::getTableMaxNumber)
                .max(Integer::compareTo).orElse(null);
        if (maxSeat == null || maxSeat < requestDto.getPersonnelCount()) {
            throw new CustomException(ErrorCode.NO_SEAT);
        }

        // TODO : 임시휴무날짜는 아닌가?
        boolean holiday = store.getStoreRestList().stream().anyMatch(storeRest -> storeRest.getStoreOffDay() == requestDto.getReservationDate().toLocalDate());
        if (holiday) {
            throw new CustomException(ErrorCode.STORE_HOLIDAY);
        }

        // TODO : 해당 요일의 영업시간 및 영엉삽태가 충족하는가?
        String dayOff = requestDto.getReservationDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN); //월, 화, 수 .. 이런식
        // 정기 휴무일을 체크 -> 받아온 값의 요일과 동일한 요일의 status값이 closed인지 체크

        boolean isDayData = store.getStoreHourList().stream().noneMatch(storeHour -> storeHour.getDayOfWeek().getDayOfWeekName().equals(dayOff));
        if (isDayData) {
            throw new CustomException(ErrorCode.NOT_FOUND_DAY);
        }

        boolean regularHoliday = store.getStoreHourList().stream().anyMatch(storeHour -> storeHour.getDayOfWeek().getDayOfWeekName().equals(dayOff) && storeHour.getDayStatus().equals(DayStatus.CLOSED));
        if (regularHoliday) {
            throw new CustomException(ErrorCode.STORE_HOLIDAY);
        }

        boolean canMake = store.getStoreTimeSlotList().stream().anyMatch(storeTimeSlot -> storeTimeSlot.getReservationTime().equals(requestDto.getReservationDate().toLocalTime()));
        if (!canMake) {
            throw new CustomException(ErrorCode.RESERVATION_TIME_ERROR);
        }

        // TODO : 받아온 메뉴 리스트가 해당 가게 안에 모두 있는가?
        List<HashMap<String, Long>> menuList = requestDto.getMenuList();

        List<Long> menuIds = menuList.stream()
                .map(menu -> menu.get("menuId"))
                .toList();

        List<Long> menuCounts = menuList.stream()
                .map(menu -> menu.get("menuCount"))
                .toList();
        boolean existMenu = menuIds.stream()
                .allMatch(menuId -> store.getMenuList().stream()
                        .anyMatch(storeMenu -> storeMenu.getMenuId().equals(menuId)));
        if (!existMenu) {
            throw new CustomException(ErrorCode.NOT_FOUND_MENU);
        }

        // TODO : 인원수에 해당하는 자리가 남아 있는가?  -> 예약 불가 : 전화 문의하기
        Integer requestCount = requestDto.getPersonnelCount();
        Integer requestTableSize = requestCount;
        // 테이블 사이즈가 예약 인원수와 같은 예약의 숫자를 일단 저장
        Integer tablesize = Math.toIntExact(store.getReservationList().stream().filter(reservation ->
                reservation.getReservationDate().equals(requestDto.getReservationDate().toLocalDate()) &&
                        reservation.getReservationTime().equals(requestDto.getReservationDate().toLocalTime()) &&
                        reservation.getTableSize() == requestDto.getPersonnelCount() &&
                        !reservation.getReservationStatus().equals(ReservationStatus.CANCELED)).count()
        );

        Integer tablesizeLarge = Math.toIntExact(
                store.getReservationList().stream()
                        .filter(reservation ->
                                reservation.getReservationDate().equals(requestDto.getReservationDate().toLocalDate()) &&
                                        reservation.getReservationTime().equals(requestDto.getReservationDate().toLocalTime()) &&
                                        reservation.getTableSize() == requestCount + 1 &&
                                        !reservation.getReservationStatus().equals(ReservationStatus.CANCELED))
                        .count()
        );

        // 인원수에 맞는 테이블로 예약 가능한지 확인하기, 안된다면 +1까지 검토
        boolean canSeat = store.getStoreTableList().stream()
                .anyMatch(storeTable -> storeTable.getTableMaxNumber().equals(requestCount) && storeTable.getTableCount() - tablesize >= 1);

        boolean canSeat2 = store.getStoreTableList().stream()
                .anyMatch(storeTable -> storeTable.getTableMaxNumber().equals(requestCount + 1) && storeTable.getTableCount() - tablesizeLarge >= 1);
        if (!canSeat) {
            if (canSeat2) {
                requestTableSize = requestTableSize + 1; // 3인으로 왔는데 3인이 없는경우 4인을 검사해서 4인이 있다? -> 4인테이블로 예약하기 위해 4인을 잠깐 저장
            } else {
                throw new CustomException(ErrorCode.NO_SEAT);
            }
        }

        // TODO : OK 그럼 예약 생성해줄게
        Party party = partyRepository.save(new Party());
        Reservation reservation = new Reservation(requestDto.getReservationDate().toLocalDate(), requestDto.getReservationDate().toLocalTime(), requestCount, store, party, requestTableSize);
        reservationRepository.save(reservation);
        PartyPeople partyPeople = new PartyPeople(party, user, PartyRole.REPRESENTATIVE);
        partyPeopleRepository.save(partyPeople);
        List<ReturnMenuListDto> returnMenuListDtos = new ArrayList<>();
        for (int i = 0; i < menuIds.size(); i++) {
            if (menuCounts.get(i) <= 0) {
                continue;
            }
            Menu menuItem = menuRepository.findById(menuIds.get(i)).orElse(null);
            ReservationMenu reservationMenu = new ReservationMenu(menuItem, reservation, menuCounts.get(i));
            reservationMenuRepository.save(reservationMenu);
            ReturnMenuListDto returnMenuListDto = new ReturnMenuListDto(Objects.requireNonNull(menuItem).getMenuId(), menuCounts.get(i), menuItem.getMenuName());
            returnMenuListDtos.add(returnMenuListDto);
        }

        return new ReservationCreateResponseDto(user.getId(), reservation.getReservationId(), requestDto.getReservationDate().toLocalDate()
                , requestDto.getReservationDate().toLocalTime(), requestCount, returnMenuListDtos);
    }


    /**
     * 예약 전체 조회 메서드
     *
     * @param user
     * @return
     */
    public List<ReservationGetAllResponseDto> getAllReservations(User user, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservationPage = reservationRepository.findByUser(user, pageable);

        List<ReservationGetAllResponseDto> resDto = new ArrayList<>();
        reservationPage.forEach(reservation -> {
            ReservationGetAllResponseDto reservationGetAllResponseDto = new ReservationGetAllResponseDto(reservation);
            resDto.add(reservationGetAllResponseDto);
        });

        return resDto;
    }

    /**
     * 예약 단건 조회 메서드
     *
     * @param reservationId
     * @param user
     * @return ReservationGetOneResponseDto
     */
    public ReservationGetOneResponseDto getReservation(Long reservationId, User user) {

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        // 지정된 예약에 로그인된 유저가 포함되어 있는지 검사
        reservation.getParty().getPartyPeople().stream()
                .filter(partyPeople -> partyPeople.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST));

        List<ReturnMenuListDto> menuList = new ArrayList<>();
        reservation.getReservationMenuList()
                .forEach(ml -> {
                    ReturnMenuListDto menus = new ReturnMenuListDto(ml.getMenu().getMenuId(), ml.getMenuCount(), ml.getMenu().getMenuName());
                    menuList.add(menus);
                });

        boolean isExist = reviewRepository.existsByUserIdAndTargetIdAndReviewTarget(user.getId(), reservationId, ReviewTarget.RESERVATION);


        return new ReservationGetOneResponseDto(reservation, menuList, isExist);
    }

    /**
     * 예약 메뉴 변경 메서드
     *
     * @param reservationId
     * @param user
     * @param requestDto
     * @return
     */
    @Transactional
    public ReservationGetOneResponseDto updateReservation(Long reservationId, User user, ReservationUpdateRequestDto requestDto) {

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        Store store = reservation.getStore();

        if(!reservation.getReservationStatus().equals(ReservationStatus.PENDING)){throw new CustomException(ErrorCode.FORBIDDEN_RESERVATION_END);}

        // 요청 DTO에서 menuList를 가져옴
        List<HashMap<String, Long>> menuList = requestDto.getMenuList();
        List<ReturnMenuListDto> returnMenuLists = new ArrayList<>();

        if (menuList != null && !menuList.isEmpty() &&
                menuList.stream().anyMatch(menu -> menu.get("menuCount") != null && menu.get("menuCount") > 0)) {

            reservationMenuRepository.deleteAllByReservation(reservation);

            List<Menu> menus = new ArrayList<>();
            List<Long> menuCounts = new ArrayList<>();

            menuList.forEach(menu -> {
                Long menuId = menu.get("menuId"); // 예약한 메뉴의 아이디값
                Long menuCount = menu.get("menuCount");

                if (menuCount <= 0) {
                    return;
                }

                // 메뉴가 존재하는지 확인
                boolean isMenuExist = menuRepository.existsByMenuIdAndStore(menuId, store);
                if (!isMenuExist) {
                    throw new BadRequestException(ErrorCode.NOT_FOUND_MENU);
                }

                // 메뉴를 추가하고 반환 리스트를 구성
                Menu addMenu = menuRepository.findByIdOrElseThrow(menuId);
                menus.add(addMenu);
                menuCounts.add(menuCount);
                ReturnMenuListDto returnMenuList = new ReturnMenuListDto(menuId, menuCount, addMenu.getMenuName());
                returnMenuLists.add(returnMenuList);
            });

            // 메뉴와 수량을 저장
            for (int i = 0; i < menus.size(); i++) {
                ReservationMenu reservationMenu = new ReservationMenu(menus.get(i), reservation, menuCounts.get(i));
                reservationMenuRepository.save(reservationMenu);
            }
        } else {
            reservation.getReservationMenuList().forEach(reservationMenu -> {
                ReturnMenuListDto returnMenuList = new ReturnMenuListDto(
                        reservationMenu.getMenu().getMenuId(),
                        reservationMenu.getMenuCount(),
                        reservationMenu.getMenu().getMenuName()
                );
                returnMenuLists.add(returnMenuList);
            });
        }

        boolean isExist =
                reviewRepository.existsByUserIdAndTargetIdAndReviewTarget(user.getId(), reservationId, ReviewTarget.RESERVATION);

        return new ReservationGetOneResponseDto(reservation, returnMenuLists, isExist);
    }


    /**
     * 예약 취소 메서드
     *
     * @param reservationId
     */
    @Transactional
    public void cancelReservation(Long reservationId, User user) {

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        // 예약에 등록된 유저가 아닌경우
        User userData = reservation.getParty().getPartyPeople().stream().map(PartyPeople::getUser).filter(partyPeopleUser -> partyPeopleUser.getId().equals(user.getId())).findFirst().orElse(null);
        if (userData == null) {
            throw new BadRequestException(ErrorCode.REJECT_CANCEL);
        }
        reservation.modifyStatus(ReservationStatus.CANCELED);

        reservationRepository.save(reservation); // 예약 정보만 취소로 바꾸고 나머지는 리포지토리에서 삭제

    }

    /**
     *  가게 오너 입장에서 예약 조회 메서드
     * @param reservationDate
     * @param storeId
     * @return List<OwnerReservationResponseDto>
     */
    public List<OwnerReservationResponseDto> getStoreReservations(LocalDate reservationDate, Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        List<Reservation> reservations = reservationRepository.findAllByReservationDateAndStore(reservationDate,store);
        List<OwnerReservationResponseDto> responseDtos = new ArrayList<>();
        // TODO : 얘는 보여야 할게 예약 날짜, 예약시간, 예약자 대표, 인원수 정도만 보이면 되겟다
        for(Reservation reservation : reservations){
            OwnerReservationResponseDto ownerReservationResponseDto = new OwnerReservationResponseDto(reservation);
            responseDtos.add(ownerReservationResponseDto);
        }

        return responseDtos;
    }

    /**
     * 가게별 인원수 100명 체크 메서드
     * @param storeId
     * @return
     */
    public boolean tryReserve(Long storeId) {
        String key = "reservation_limit:" + storeId;
        RAtomicLong reservationCount = redissonClient.getAtomicLong(key);

        long currentCount = reservationCount.incrementAndGet();

        if (currentCount > 100) {
            reservationCount.decrementAndGet(); // 롤백
            return false;
        }

        return true;
    }
}
