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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.TextStyle;
import java.util.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final PartyRepository partyRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final PartyPeopleRepository partyPeopleRepository;

    public ReservationService(ReservationRepository reservationRepository, StoreRepository storeRepository,
                              MenuRepository menuRepository, PartyRepository partyRepository,
                              ReservationMenuRepository reservationMenuRepository,
                              PartyPeopleRepository partyPeopleRepository) {
        this.reservationRepository = reservationRepository;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.partyRepository = partyRepository;
        this.reservationMenuRepository = reservationMenuRepository;
        this.partyPeopleRepository = partyPeopleRepository;
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
        Store store = storeRepository.findByIdOrElseThrow(storeId);

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
        // 인원수를 세는게 아니라 인원수를 가지고 와서 해당 인원수로 등록된 예약만큼 빼기
        Integer requestCount = requestDto.getPersonnelCount();
        Integer requestTableSize = requestCount;
        // 테이블 사이즈가 예약 인원수와 같은 예약의 숫자를 일단 저장
        Integer tablesize = Math.toIntExact(store.getReservationList().stream().filter(reservation ->
                reservation.getReservationDate().equals(requestDto.getReservationDate().toLocalDate()) &&
                reservation.getReservationTime().equals(requestDto.getReservationDate().toLocalTime()) &&
                reservation.getTableSize() == requestDto.getPersonnelCount()).count());

        // 인원수에 맞는 테이블로 예약 가능한지 확인하기, 안된다면 +1까지 검토
        boolean canSeat = store.getStoreTableList().stream()
                .anyMatch(storeTable -> storeTable.getTableMaxNumber().equals(requestCount) && storeTable.getTableCount() - tablesize >= 1);

        boolean canSeat2 = store.getStoreTableList().stream()
                .anyMatch(storeTable -> storeTable.getTableMaxNumber().equals(requestCount + 1) && storeTable.getTableCount() - tablesize >= 1);
        if (!canSeat) {
            if(canSeat2){
                requestTableSize = requestTableSize + 1; // 3인으로 왔는데 3인이 없는경우 4인을 검사해서 4인이 있다? -> 4인테이블로 예약하기 위해 4인을 잠깐 저장
            }else{
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
            Menu menuItem = menuRepository.findById(menuIds.get(i)).orElse(null);
            ReservationMenu reservationMenu = new ReservationMenu(menuItem, reservation, menuCounts.get(i));
            reservationMenuRepository.save(reservationMenu);
            ReturnMenuListDto returnMenuListDto = new ReturnMenuListDto(Objects.requireNonNull(menuItem).getMenuId(), menuCounts.get(i), menuItem.getMenuName());
            returnMenuListDtos.add(returnMenuListDto);
        }


        return new ReservationCreateResponseDto(reservation.getReservationId(), requestDto.getReservationDate().toLocalDate()
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
                .filter(partyPeople -> partyPeople.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST));

        List<ReturnMenuListDto> menuList = new ArrayList<>();
        reservation.getReservationMenuList()
                .forEach(ml -> {
                    ReturnMenuListDto menus = new ReturnMenuListDto(ml.getMenu().getMenuId(), ml.getMenuCount(), ml.getMenu().getMenuName());
                    menuList.add(menus);
                });


        return new ReservationGetOneResponseDto(reservation, user, menuList);
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
        PartyPeople reservationUser = reservation.getParty().getPartyPeople().stream()
                .filter(partyPeople -> partyPeople.getUser().equals(user) && partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE)).findFirst().orElse(null);

        Store store = reservation.getStore();
        reservationMenuRepository.deleteById(reservationId);
        List<HashMap<String, Long>> menuList = requestDto.getMenuList();
        List<ReturnMenuListDto> returnMenuLists = new ArrayList<>();

        List<Menu> menus = new ArrayList<>();
        List<Long> menuCounts = new ArrayList<>();

        menuList.forEach(menu -> {
            Long menuId = menu.get("menuId"); // 예약한 메뉴의 아이디값을 가져옴
            Long menuCount = menu.get("menuCount");

            boolean isMenuExist = menuRepository.existsByMenuIdAndStore(menuId, store);

            if (!isMenuExist) {
                throw new BadRequestException(ErrorCode.NOT_FOUND_MENU);
            }
            Menu addMenu = menuRepository.findByIdOrElseThrow(menuId);
            menus.add(addMenu);
            menuCounts.add(menuCount);
            ReturnMenuListDto returnMenuList = new ReturnMenuListDto(menuId, menuCount, addMenu.getMenuName());
            returnMenuLists.add(returnMenuList);

        });

        for (int i = 0; i < menus.size(); i++) {
            ReservationMenu reservationMenu = new ReservationMenu(menus.get(i), reservation, menuCounts.get(i));
            reservationMenuRepository.save(reservationMenu);
        }

        return new ReservationGetOneResponseDto(reservation, user, returnMenuLists);
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
        User userData = reservation.getParty().getPartyPeople().stream().map(PartyPeople::getUser).filter(partyPeopleUser -> partyPeopleUser.equals(user)).findFirst().orElse(null);
        if (userData == null) {
            throw new BadRequestException(ErrorCode.REJECT_CANCEL);
        }
        reservation.modifyStatus(ReservationStatus.CANCELED);

        reservationRepository.save(reservation); // 예약 정보만 취소로 바꾸고 나머지는 리포지토리에서 삭제

    }


}
