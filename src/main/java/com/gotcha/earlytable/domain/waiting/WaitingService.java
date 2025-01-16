package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.party.PartyPeopleRepository;
import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.DayStatus;
import com.gotcha.earlytable.domain.store.enums.ReservationType;
import com.gotcha.earlytable.domain.store.storeHour.StoreHourRepository;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.dto.*;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.domain.waitingsetting.WaitingSettingRepository;
import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import com.gotcha.earlytable.global.enums.*;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.CustomException;
import com.gotcha.earlytable.global.error.exception.ForbiddenException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final PartyRepository partyRepository;
    private final PartyPeopleRepository partyPeopleRepository;
    private final UserRepository userRepository;
    private final WaitingSettingRepository waitingSettingRepository;
    private final StoreHourRepository storeHourRepository;

    public WaitingService(WaitingRepository waitingRepository, StoreRepository storeRepository,
                          PartyRepository partyRepository, PartyPeopleRepository partyPeopleRepository,
                          UserRepository userRepository, WaitingSettingRepository waitingSettingRepository,
                          StoreHourRepository storeHourRepository) {

        this.waitingRepository = waitingRepository;
        this.storeRepository = storeRepository;
        this.partyRepository = partyRepository;
        this.partyPeopleRepository = partyPeopleRepository;
        this.userRepository = userRepository;
        this.waitingSettingRepository = waitingSettingRepository;
        this.storeHourRepository = storeHourRepository;
    }

    /**
     * 원격 웨이팅 생성 메서드
     *
     * @param requestDto
     * @param storeId
     * @return WaitingResponseDto
     */
    @Transactional
    public WaitingResponseDto createWaitingOnline(WaitingOnlineRequestDto requestDto, Long storeId, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 예약 가능 여부 확인
        checkWaiting(store, requestDto.getWaitingType(), ReservationType.REMOTE);

        // 일행 그룹 생성
        Party party = partyRepository.save(new Party());

        // 일행 인원 등록
        PartyPeople partyPeople = new PartyPeople(party, user, PartyRole.REPRESENTATIVE);
        partyPeopleRepository.save(partyPeople);

        // 오늘 날짜 가져오기
        LocalDate date = LocalDate.now();

        // 웨이팅 번호
        int waitingNumber = waitingRepository.countByStoreAndCreatedAtBetweenAndWaitingType(store,
                date.atTime(0, 0, 0), date.atTime(23, 59, 59), requestDto.getWaitingType());

        waitingNumber++;

        // 웨이팅 생성
        Waiting waiting = new Waiting(store, party, requestDto.getWaitingType(), requestDto.getPersonnelCount(),
                WaitingStatus.PENDING, RemoteStatus.REMOTE, waitingNumber, user.getPhone());

        // 저장
        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingResponseDto(savedWaiting);
    }

    /**
     * 현장 웨이팅 생성 메서드
     *
     * @param requestDto
     * @param storeId
     * @return WaitingNumberResponseDto
     */
    @Transactional
    public WaitingNumberResponseDto createWaitingOffline(WaitingOfflineRequestDto requestDto, Long storeId) {
        // 가게 확인
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 예약 가능 여부 확인
        checkWaiting(store, requestDto.getWaitingType(), ReservationType.ONSITE);

        // 전화번호로 유저 가져오기
        Optional<User> user = userRepository.findByPhone(requestDto.getPhoneNumber());

        Party party = null;

        // 이미 존재하는 유저일 경우
        if (user.isPresent()) {
            party = partyRepository.save(new Party());

            PartyPeople partyPeople = new PartyPeople(party, user.get(), PartyRole.REPRESENTATIVE);
            // 일행 인원 등록
            partyPeopleRepository.save(partyPeople);
        }

        // 오늘 날짜 가져오기
        LocalDate date = LocalDate.now();

        // 웨이팅 번호
        int waitingNumber = waitingRepository.countByStoreAndCreatedAtBetweenAndWaitingType(store,
                date.atTime(0, 0, 0), date.atTime(23, 59, 59), requestDto.getWaitingType());

        waitingNumber++;

        // 웨이팅 생성
        Waiting waiting = new Waiting(store, party, requestDto.getWaitingType(), requestDto.getPersonnelCount(),
                WaitingStatus.PENDING, RemoteStatus.REMOTE, waitingNumber, requestDto.getPhoneNumber());

        waitingRepository.save(waiting);

        return new WaitingNumberResponseDto(waitingNumber);
    }

    /**
     * 웨이팅 목록 조회 메서드
     *
     * @param user
     * @return List<WaitingListResponseDto>
     */
    @Transactional
    public List<WaitingResponseDto> getWaitingList(User user) {

        // 로그인 된 사용자로 PartyPeople 리스트 조회
        List<PartyPeople> partyPeopleList = partyPeopleRepository.findByUser(user);

        // PartyPeople 리스트에서 Waiting 추출
        List<Waiting> waitingList = partyPeopleList.stream()
                .map(PartyPeople::getParty)        // Party 추출
                .map(Party::getWaiting)           // Party에서 Waiting 추출
                .filter(Objects::nonNull)         // null 값 제외
                .filter(waiting -> waiting.getWaitingStatus() != WaitingStatus.DELAY) // waitingStatus가 "delay"인 항목 제외
                .toList();

        return waitingList.stream()
                .map(WaitingResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 웨이팅 미루기 메서드
     *
     * @param waitingId
     * @param user
     * @return WaitingNumberResponseDto
     */
    @Transactional
    public WaitingNumberResponseDto delayWaiting(Long waitingId, User user) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        // 로그인한 유저가 웨이팅 등록자인지 확인
        waiting.getParty().getPartyPeople().stream()
                .filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE)) // role이 REPRESENTATIVE인 PartyPeople 필터링
                .map(PartyPeople::getUser) // PartyPeople에서 User 객체로 변환
                .filter(checkUser -> checkUser.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.FORBIDDEN_PERMISSION));

        waiting.updateWaiting(WaitingStatus.DELAY);
        waitingRepository.save(waiting);


        // 웨이팅 날짜만 가져오기
        LocalDate waitingDate = waiting.getCreatedAt().toLocalDate();

        // 웨이팅 번호
        int waitingNumber = waitingRepository.countByStoreAndCreatedAtBetweenAndWaitingType(waiting.getStore(),
                waitingDate.atTime(0, 0, 0), waitingDate.atTime(23, 59, 59), waiting.getWaitingType());

        waitingNumber++;

        Waiting newWaiting = new Waiting(waiting.getStore(), waiting.getParty(), waiting.getWaitingType(), waiting.getPersonnelCount(),
                WaitingStatus.PENDING, RemoteStatus.REMOTE, waitingNumber, user.getPhone());

        waitingRepository.save(newWaiting);

        return new WaitingNumberResponseDto(waitingNumber);
    }

    /**
     * 웨이팅 상세 조회 메서드
     *
     * @param waitingId
     * @param user
     * @return WaitingDetailResponseDto
     */
    @Transactional
    public WaitingGetOneResponseDto getWaitingDetail(Long waitingId, User user) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        // 사용자 권한이 일반 유저이면 해당 웨이팅에 권한이 있는지 확인
        if (user.getAuth() == Auth.USER) {
            Optional.ofNullable(waiting.getParty())
                    .orElseThrow(() -> new BadRequestException(ErrorCode.UNAUTHORIZED))
                    .getPartyPeople().stream()
                    .filter(partyPeople -> partyPeople.getUser().getId().equals(user.getId()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(ErrorCode.FORBIDDEN_PERMISSION));
        }

        // 사용자 권한이 사장님이면
        if (user.getAuth() == Auth.OWNER && !waiting.getStore().getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        return new WaitingGetOneResponseDto(waiting);
    }

    /**
     * 웨이팅 조회 (Owner)
     *
     * @param user
     * @param storeId
     * @param requestDto
     * @return WaitingOwnerResponseDto
     */
    public WaitingOwnerResponseDto getOwnerNowWaitingList(User user, Long storeId, @Valid WaitingSimpleOwnerRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if (!store.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        List<Waiting> waitingList = waitingRepository.findByStoreAndWaitingTypeAndWaitingStatus(store, requestDto.getWaitingType(), WaitingStatus.PENDING);

        return new WaitingOwnerResponseDto(waitingList, requestDto.getWaitingType(), "now");
    }

    public WaitingOwnerResponseDto getOwnerWaitingList(User user, Long storeId, @Valid WaitingOwnerRequestDto requestDto) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if (!store.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        List<Waiting> waitingList = waitingRepository.findByStoreAndWaitingTypeAndWaitingStatusNot(store, requestDto.getWaitingType(), WaitingStatus.DELAY);

        return new WaitingOwnerResponseDto(waitingList, requestDto.getWaitingType(), "detail");
    }

    /**
     * 웨이팅 취소 메서드
     *
     * @param waitingId
     * @param user
     */
    @Transactional
    public void cancelWaiting(Long waitingId, User user) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        // 로그인한 유저가 웨이팅 등록자인지 확인
        if (user.getAuth() == Auth.USER) {
            waiting.getParty().getPartyPeople().stream()
                    .filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE)) // role이 REPRESENTATIVE인 PartyPeople 필터링
                    .map(PartyPeople::getUser) // PartyPeople에서 User 객체로 변환
                    .filter(checkUser -> checkUser.getId().equals(user.getId()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(ErrorCode.FORBIDDEN_PERMISSION));
        }

        // 예약 대기 상태만 취소 가능
        if (waiting.getWaitingStatus() != WaitingStatus.PENDING) {
            throw new BadRequestException(ErrorCode.REJECT_CANCEL);
        }

        // 웨이팅 상태 변경
        waiting.cancelWaiting();

        waitingRepository.save(waiting);
    }

    /**
     * 웨이팅 상태 변경 메서드
     *
     * @param waitingId
     * @param waitingStatus
     */
    @Transactional
    public void changeWaitingStatus(Long waitingId, WaitingStatus waitingStatus, User user) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        if (user.getAuth() == Auth.OWNER && !waiting.getStore().getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        waiting.updateWaiting(waitingStatus);

        waitingRepository.save(waiting);
    }


    /**
     * 실시간 웨이팅 순서 조회 메서드
     *
     * @param waitingId
     * @return WaitingNowSeqNumberResponseDto
     */
    public WaitingNumberResponseDto getNowSeqNumber(Long waitingId) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        // 웨이팅 날짜만 가져오기
        LocalDate waitingDate = waiting.getCreatedAt().toLocalDate();

        // 앞에 대기중인 웨이팅 개수 가져오기
        int nowSeqNum = waitingRepository.countByStoreAndWaitingStatusAndCreatedAtBetweenAndWaitingNumberLessThanEqual(
                waiting.getStore(),
                WaitingStatus.PENDING,
                waitingDate.atTime(0, 0, 0), waitingDate.atTime(23, 59, 59),
                waiting.getWaitingNumber()
        );

        return new WaitingNumberResponseDto(nowSeqNum);
    }


    /**
     * 예약 가능 여부 확인
     *
     * @param store
     * @param waitingType
     * @param reservationType
     */
    private void checkWaiting(Store store, WaitingType waitingType, ReservationType reservationType) {

        // 가게 예약 타입 확인
        boolean dontReservation = store.getStoreReservationTypeList().stream()
                .noneMatch(storeReservationType -> storeReservationType.getReservationType() == reservationType &&
                        storeReservationType.canWaiting(waitingType));

        if (dontReservation) {
            throw new CustomException(ErrorCode.UNAVAILABLE_Onsite_Waiting_TYPE);
        }

        // 휴무 여부 확인 (정기 휴무요일 & 임시 휴일)
        boolean holiday = storeHourRepository.findByStoreAndDayStatus(store, DayStatus.CLOSED).stream()
                .anyMatch(storeHour -> Objects.equals(storeHour.getDayOfWeek().getDayOfWeekName(), LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN))) ||
                store.getStoreRestList().stream().anyMatch(storeRest -> Objects.equals(storeRest.getStoreOffDay(), LocalDate.now()));
        if (holiday) {
            throw new CustomException(ErrorCode.STORE_HOLIDAY);
        }


        // 웨이팅 가능 여부 확인
        WaitingSetting waitingSetting = waitingSettingRepository.findByStore(store);
        if (waitingSetting.getWaitingSettingStatus().equals(WaitingSettingStatus.CLOSE)) {
            throw new BadRequestException(ErrorCode.WAITING_ERROR);
        }

        // 웨이팅 가능 시간 확인
        if (waitingSetting.getWaitingOpenTime().isAfter(LocalTime.now()) || waitingSetting.getWaitingClosedTime().isBefore(LocalTime.now())) {
            throw new BadRequestException(ErrorCode.WAITING_ERROR);
        }
    }


}
