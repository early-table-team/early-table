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
import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
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
     *  예약잡기 메서드
     * @param storeId
     * @param requestDto
     * @param user
     * @return  ReservationCreateResponseDto
     */
    @Transactional
    public void createReservation(Long storeId, ReservationCreateRequestDto requestDto, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // TODO : 가게 예약 타입이 예약이 맞는가?


        // TODO : 휴무날짜는 아닌가?


        // TODO : 해당 요일의 영업시간 및 영엉삽태가 충족하는가?



        // TODO : 받아온 메뉴 리스트가 해당 가게 안에 모두 있는가?



        // TODO : 인원수에 해당하는 자리가 남아 있는가?  -> 예약 불가 : 전화 문의하기


        // TODO : OK 그럼 예약 생성해줄게



        // TODO : 남은 자리수 업데이트 하기
    }

    /**
     *  예약 전체 조회 메서드
     * @param user
     * @return
     */
    public List<ReservationGetAllResponseDto> getAllReservations(User user) {

        List<Reservation> reservations = reservationRepository.findByUser(user);
        List<ReservationGetAllResponseDto> resDto = new ArrayList<>();
        reservations.forEach(reservation -> {
            ReservationGetAllResponseDto reservationGetAllResponseDto = new ReservationGetAllResponseDto(reservation);
            resDto.add(reservationGetAllResponseDto);
        });

        return resDto;
    }

    /**
     *  예약 단건 조회 메서드
     * @param reservationId
     * @param user
     * @return  ReservationGetOneResponseDto
     */
    public ReservationGetOneResponseDto getReservation(Long reservationId, User user) {

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        // 지정된 예약에 로그인된 유저가 포함되어 있는지 검사
        reservation.getParty().getPartyPeople().stream()
                .filter(partyPeople -> partyPeople.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.BAD_REQUEST));

        List<HashMap<String, Long>> menuList = new ArrayList<>();
        reservation.getReservationMenuList()
                .forEach(ml -> {
                    HashMap<String, Long> menu = new HashMap<>();
                    menu.put(ml.getMenu().getMenuName(), ml.getMenuCount());
                    menuList.add(menu);
                });

        return new ReservationGetOneResponseDto(reservation, user, menuList);
    }

    /**
     *  예약 메뉴 변경 메서드
     * @param reservationId
     * @param user
     * @param requestDto
     * @return
     */
    @Transactional
    public ReservationGetOneResponseDto updateReservation(Long reservationId, User user, ReservationUpdateRequestDto requestDto) {

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
        Store store = reservation.getStore();
        reservationMenuRepository.deleteById(reservationId);
        List<HashMap<String, Long>> menuList = requestDto.getMenuList();

        List<Menu> menus = new ArrayList<>();
        List<Long> menuCounts = new ArrayList<>();

        menuList.forEach(menu -> {
            Long menuId = menu.get("menuId"); // 예약한 메뉴의 아이디값을 가져옴
            Long menuCount = menu.get("menuCount");

            boolean isMenuExist = menuRepository.existsByMenuIdAndStore(menuId, store);

            if(!isMenuExist){
                throw new BadRequestException(ErrorCode.BAD_REQUEST);
            }
            Menu addMenu = menuRepository.findByIdOrElseThrow(menuId);
            menus.add(addMenu);
            menuCounts.add(menuCount);
        });

        for(int i = 0; i < menus.size(); i++){
            ReservationMenu reservationMenu = new ReservationMenu(menus.get(i), reservation, menuCounts.get(i));
            reservationMenuRepository.save(reservationMenu);
        }

        return new ReservationGetOneResponseDto(reservation, user, requestDto.getMenuList());
    }

    /**
     *  예약 취소 메서드
     * @param reservationId
     */
    @Transactional
    public void cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);

        Integer personnelCount = reservation.getPersonnelCount();
    }


}
