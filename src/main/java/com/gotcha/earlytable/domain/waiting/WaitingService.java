package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.party.PartyPeopleRepository;
import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.review.ReviewRepository;
import com.gotcha.earlytable.domain.review.enums.ReviewTarget;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final WaitingSequenceService waitingSequenceService;
    private final ReviewRepository reviewRepository;

    public WaitingService(WaitingRepository waitingRepository, StoreRepository storeRepository,
                          PartyRepository partyRepository, PartyPeopleRepository partyPeopleRepository,
                          UserRepository userRepository, WaitingSettingRepository waitingSettingRepository,
                          StoreHourRepository storeHourRepository, WaitingSequenceService waitingSequenceService, ReviewRepository reviewRepository) {

        this.waitingRepository = waitingRepository;
        this.storeRepository = storeRepository;
        this.partyRepository = partyRepository;
        this.partyPeopleRepository = partyPeopleRepository;
        this.userRepository = userRepository;
        this.waitingSettingRepository = waitingSettingRepository;
        this.storeHourRepository = storeHourRepository;
        this.waitingSequenceService = waitingSequenceService;
        this.reviewRepository = reviewRepository;
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
        Long waitingNumber = waitingRepository.countByStoreAndCreatedAtBetweenAndWaitingType(store,
                date.atTime(0, 0, 0), date.atTime(23, 59, 59), requestDto.getWaitingType());

        waitingNumber++;

        // 웨이팅 생성
        Waiting waiting = new Waiting(store, party, requestDto.getWaitingType(), requestDto.getPersonnelCount(),
                WaitingStatus.PENDING, RemoteStatus.REMOTE, waitingNumber, user.getPhone());

        // 저장
        Waiting savedWaiting = waitingRepository.save(waiting);

        // 웨이팅 순서에 포함
        waitingSequenceService.addToWaitingQueue(savedWaiting);

        // 현재 대기 팀 수 저장
        waitingSequenceService.saveWaitingLeft(savedWaiting);

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
        Long waitingNumber = waitingRepository.countByStoreAndCreatedAtBetweenAndWaitingType(store,
                date.atTime(0, 0, 0), date.atTime(23, 59, 59), requestDto.getWaitingType());

        waitingNumber++;

        // 웨이팅 생성
        Waiting waiting = new Waiting(store, party, requestDto.getWaitingType(), requestDto.getPersonnelCount(),
                WaitingStatus.PENDING, RemoteStatus.SITE, waitingNumber, requestDto.getPhoneNumber());

        Waiting savedWaiting =  waitingRepository.save(waiting);

        // 웨이팅 순서에 포함
        waitingSequenceService.addToWaitingQueue(savedWaiting);
        waitingSequenceService.saveWaitingLeft(savedWaiting);

        // 예상 대기 시간 조회
        Integer waitingTime = waitingSequenceService.getTakenTimeWaiting(savedWaiting);

        return new WaitingNumberResponseDto(waiting.getWaitingId(),waitingNumber, waitingTime);
    }

    /**
     * 웨이팅 목록 조회 메서드
     *
     * @param user
     * @return List<WaitingListResponseDto>
     */
    @Transactional
    public List<WaitingResponseDto> getWaitingList(User user, Pageable pageable) {

        Page<Waiting> waitingList
                = waitingRepository.findByPartyPartyPeopleUserAndWaitingStatusNotOrderByCreatedAtDesc(user, pageable, WaitingStatus.DELAY);

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

        // 웨이팅 순서에서 제거
        waitingSequenceService.removeFromWaitingQueue(waiting);

        // 웨이팅 날짜만 가져오기
        LocalDate waitingDate = waiting.getCreatedAt().toLocalDate();

        // 웨이팅 번호
        Long waitingNumber = waitingRepository.countByStoreAndCreatedAtBetweenAndWaitingType(waiting.getStore(),
                waitingDate.atTime(0, 0, 0), waitingDate.atTime(23, 59, 59), waiting.getWaitingType());

        waitingNumber++;
        // 일행 그룹 생성
        Party party = partyRepository.save(new Party());

        // 일행 인원 등록
        PartyPeople partyPeople = new PartyPeople(party, user, PartyRole.REPRESENTATIVE);
        partyPeopleRepository.save(partyPeople);

        Waiting newWaiting = new Waiting(waiting.getStore(), party, waiting.getWaitingType(), waiting.getPersonnelCount(),
                WaitingStatus.PENDING, RemoteStatus.REMOTE, waitingNumber, user.getPhone());


        waitingRepository.save(newWaiting);

        // 웨이팅 순서에 포함
        waitingSequenceService.addToWaitingQueue(newWaiting);
        Integer waitingTime = waitingSequenceService.getTakenTimeWaiting(newWaiting);

        return new WaitingNumberResponseDto(waiting.getWaitingId(),waitingNumber, waitingTime);
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
        boolean isExist =
                reviewRepository.existsByUserIdAndTargetIdAndReviewTarget(user.getId(), waitingId, ReviewTarget.WAITING);

        return new WaitingGetOneResponseDto(waiting, isExist);
    }

    /**
     * 웨이팅 조회 (Owner)
     *
     * @param user
     * @param storeId
     * @param waitingType
     * @return WaitingOwnerResponseDto
     */
    public WaitingOwnerTimeResponseDto getOwnerNowWaitingList(User user, Long storeId, WaitingType waitingType) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if (!store.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        List<Waiting> waitingList = waitingRepository.findByStoreAndWaitingTypeAndWaitingStatus(store, waitingType, WaitingStatus.PENDING);

        Integer waitingTime = null;
        if (!waitingList.isEmpty()) {
            // 마지막 대기 항목을 가져옵니다.
            Waiting lastWaiting = waitingList.get(waitingList.size() - 1);

            // 웨이팅 시간을 구합니다.
            waitingTime = waitingSequenceService.getTakenTimeWaiting(lastWaiting);
        }

        return new WaitingOwnerTimeResponseDto(waitingList, waitingType, "now", waitingTime);
    }

    /**
     * 웨이팅 목록 상세 조회 (Owner)
     *
     * @param user
     * @param storeId
     * @param waitingType
     * @param date
     * @return
     */
    public WaitingOwnerResponseDto getOwnerWaitingList(User user, Long storeId, WaitingType waitingType, LocalDate date) {
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        if (!store.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        LocalDateTime startOfDay = date.atStartOfDay(); // 시작 시간
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        List<Waiting> waitingList = waitingRepository.findByStoreAndWaitingTypeAndWaitingStatusNotAndCreatedAtBetween(store, waitingType, WaitingStatus.DELAY, startOfDay, endOfDay);


        return new WaitingOwnerResponseDto(waitingList, waitingType, "detail");
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

        // 웨이팅 순서에서 제거
        waitingSequenceService.removeFromWaitingQueue(waiting);

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

        // 가게 주인인지 확인
        if (user.getAuth() == Auth.OWNER && !waiting.getStore().getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        // 미루기일 때, 상태 변경 거부
        if(waiting.getWaitingStatus().equals(WaitingStatus.DELAY)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 웨이팅 대기일 때, 입장완료 처리
        if(waiting.getWaitingStatus().equals(WaitingStatus.PENDING) && !waitingStatus.equals(WaitingStatus.PENDING)) {
            waitingSequenceService.removeFromWaitingQueue(waiting);
        }

        // 대기 중이 아닐 때, 다시 입장 대기 처리
        if (!waiting.getWaitingStatus().equals(WaitingStatus.PENDING) && waitingStatus.equals(WaitingStatus.PENDING)) {
            waitingSequenceService.addToWaitingQueue(waiting);
            waitingSequenceService.deleteTakenTimeWaiting(waiting);
        }

        // 상태 수정
        waiting.updateWaiting(waitingStatus);

        waitingRepository.save(waiting);
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
            throw new CustomException(ErrorCode.UNAVAILABLE_ONSITE_WAITING_TYPE);
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

//    /**
//     * 실시간 웨이팅 순서 조회 메서드
//     *
//     * @param waitingId
//     * @return WaitingNowSeqNumberResponseDto
//     */
//    public WaitingNumberResponseDto getNowSeqNumber(Long waitingId) {
//
//        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);
//
//        // 웨이팅 날짜만 가져오기
//        LocalDate waitingDate = waiting.getCreatedAt().toLocalDate();
//
//        // 앞에 대기중인 웨이팅 개수 가져오기
//        int nowSeqNum = waitingRepository.countByStoreAndWaitingStatusAndCreatedAtBetweenAndWaitingNumberLessThanEqual(
//                waiting.getStore(),
//                WaitingStatus.PENDING,
//                waitingDate.atTime(0, 0, 0), waitingDate.atTime(23, 59, 59),
//                waiting.getWaitingNumber()
//        );
//
//        return new WaitingNumberResponseDto(nowSeqNum);
//    }

}
