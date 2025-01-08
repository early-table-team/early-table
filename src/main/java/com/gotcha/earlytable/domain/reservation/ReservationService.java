package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.party.PartyPeopleRepository;
import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateRequestDto;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateResponseDto;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.domain.reservation.entity.ReservationMenu;
import com.gotcha.earlytable.domain.store.*;
import com.gotcha.earlytable.domain.store.entity.*;
import com.gotcha.earlytable.domain.store.enums.DayOfWeek;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final StoreReservationTypeRepository storeReservationTypeRepository;
    private final StoreHourRepository storeHourRepository;
    private final MenuRepository menuRepository;
    private final AvailableTableRepository availableTableRepository;
    private final ReservationMasterRepository reservationMasterRepository;
    private final PartyRepository partyRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final PartyPeopleRepository partyPeopleRepository;

    public ReservationService(ReservationRepository reservationRepository, StoreRepository storeRepository, StoreReservationTypeRepository storeReservationTypeRepository,
                              StoreHourRepository storeHourRepository, MenuRepository menuRepository, AvailableTableRepository availableTableRepository, ReservationMasterRepository reservationMasterRepository,
                              PartyRepository partyRepository, ReservationMenuRepository reservationMenuRepository, PartyPeopleRepository partyPeopleRepository) {
        this.reservationRepository = reservationRepository;
        this.storeRepository = storeRepository;
        this.storeReservationTypeRepository = storeReservationTypeRepository;
        this.storeHourRepository = storeHourRepository;
        this.menuRepository = menuRepository;
        this.availableTableRepository = availableTableRepository;
        this.reservationMasterRepository = reservationMasterRepository;
        this.partyRepository = partyRepository;
        this.reservationMenuRepository = reservationMenuRepository;
        this.partyPeopleRepository = partyPeopleRepository;
    }

    /**
     *
     * @param storeId
     * @param requestDto
     * @param user
     * @return  ReservationCreateResponseDto
     */
    @Transactional
    public ReservationCreateResponseDto createReservation(Long storeId, ReservationCreateRequestDto requestDto, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // TODO : 가게 예약 타입이 예약이 맞는가? -> 예약이 가능한지
        StoreReservationType storeType = storeReservationTypeRepository.findByStore(store);
        if(!ReservationType.RESERVATION.equals(storeType.getReservationType())) { // 가게가 예약이 가능한 타입이 아닌경우
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
        // TODO : 휴무날짜는 아닌가?
        LocalDate reservationDate = requestDto.getReservationDate().toLocalDate(); //휴무 날짜비교를 위해 LocalDate로 받아옴
        List<StoreDayOff> storeOffDay = store.getStoreDayOffList();
        for(StoreDayOff storeDayOff : storeOffDay){
            if(storeDayOff.getStoreOffDay() == reservationDate){ //휴무일에 예약을 하려는 경우
                throw new BadRequestException(ErrorCode.BAD_REQUEST);
            }
        }

        // TODO : 해당 요일의 영업시간 내에 충족하는가?
        String dayOfWeek = reservationDate.getDayOfWeek().toString().toUpperCase();
        // 요일에 해당하는 가게 영업시간 받아오기
        StoreHour storeHour = storeHourRepository.findByStoreAndDayOfWeek(store, DayOfWeek.valueOf(dayOfWeek));
        if(storeHour.getClosedTime().isBefore(requestDto.getReservationDate().toLocalTime()) || requestDto.getReservationDate().toLocalTime().isBefore(storeHour.getOpenTime())){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }


        // TODO : 받아온 메뉴 리스트가 해당 가게 안에 모두 있는가?
        List<HashMap<String, Long>> menuList = requestDto.getMenuList();
        List<Menu> menus = new ArrayList<>();
        List<Long> menucounts = new ArrayList<>();
        for(HashMap<String, Long> menu : menuList){

            Long menuId = menu.get("menuId"); // 예약한 메뉴의 아이디값을 가져옴
            Long menuCount = menu.get("menuCount");
            boolean isMenuExist = menuRepository.existsByMenuIdAndStore(menuId, store);

            if(!isMenuExist){
                throw new BadRequestException(ErrorCode.BAD_REQUEST);
            }
            Menu addMenu = menuRepository.findByIdOrElseThrow(menuId);
            menus.add(addMenu);
            menucounts.add(menuCount);

        }


        // TODO : 인원수에 해당하는 자리가 남아 있는가?  -> 예약 불가 : 전화 문의하기
        Integer personnelCount = requestDto.getPersonnelCount();  //요청받은 인원수
        if(personnelCount % 2 == 1){
            personnelCount++;
        } // 인원이 홀수인 경우 2,4,6,8 등의 자리수를 맞춰주기 위한작업 3,5,7인석은 없기때문

        ReservationMaster reservationMaster = reservationMasterRepository.findByTableMaxNumberAndReservationTime(personnelCount, requestDto.getReservationDate().toLocalTime());

        AvailableTable availableTable = availableTableRepository.findByReservationMaster(reservationMaster);
        if( availableTable.getRemainTable() == 0){
            throw new BadRequestException(ErrorCode.NO_SEAT);
        }

        // TODO : OK 그럼 예약 생성해줄게
        //파티 생성 후 예약 만들기 -> 예약 만들때 파티가 자동으로 동기화
        Party party = new Party();
        Reservation reservation = new Reservation(requestDto.getReservationDate().toLocalDate(), requestDto.getPersonnelCount(), store, party );

        //예약 인원 추가
        PartyPeople partyPeople = new PartyPeople(party, user, PartyRole.REPRESENTATIVE);

        //예약 메뉴 추가
        for(int i = 0; i < menus.size(); i++){
            ReservationMenu reservationMenu = new ReservationMenu(menus.get(i), reservation, menucounts.get(i));
            reservationMenuRepository.save(reservationMenu);
        }

        partyRepository.save(party);
        reservationRepository.save(reservation);
        partyPeopleRepository.save(partyPeople);


        // TODO : 남은 자리수 업데이트 하기
        availableTable.decreaseRemainTable();
        availableTableRepository.save(availableTable);

        ReservationCreateResponseDto responseDto = new ReservationCreateResponseDto(reservation.getReservationId(), requestDto.getReservationDate(),requestDto.getPersonnelCount(), requestDto.getMenuList());

        return responseDto;
    }


}
