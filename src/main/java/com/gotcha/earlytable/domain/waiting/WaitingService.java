package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.party.PartyPeopleRepository;
import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.dto.*;
import com.gotcha.earlytable.domain.waiting.entity.OfflineUser;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.enums.RemoteStatus;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final PartyRepository partyRepository;
    private final OfflineUserRepository offlineUserRepository;
    private final PartyPeopleRepository partyPeopleRepository;
    private final UserRepository userRepository;

    public WaitingService(WaitingRepository waitingRepository, StoreRepository storeRepository,
                          PartyRepository partyRepository, OfflineUserRepository offlineUserRepository,
                          PartyPeopleRepository partyPeopleRepository, UserRepository userRepository) {

        this.waitingRepository = waitingRepository;
        this.storeRepository = storeRepository;
        this.partyRepository = partyRepository;
        this.offlineUserRepository = offlineUserRepository;
        this.partyPeopleRepository = partyPeopleRepository;
        this.userRepository = userRepository;
    }

    /**
     * 원격 웨이팅 생성 메서드
     *
     * @param requestDto
     * @param storeId
     * @return WaitingOnlineResponseDto
     */
    @Transactional
    public WaitingOnlineResponseDto createWaitingOnline(WaitingOnlineRequestDto requestDto, Long storeId, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 일행 그룹 생성
        Party party = partyRepository.save(new Party());

        // 일행 인원 등록
        PartyPeople partyPeople = new PartyPeople(party, user, PartyRole.REPRESENTATIVE);
        partyPeopleRepository.save(partyPeople);

        // 오늘 날짜 가져오기
        LocalDate date = LocalDate.now();

        // 웨이팅 번호
        int waitingNumber = waitingRepository.countByStoreAndCreatedAtBetween(store,
                date.atTime(0,0,0), date.atTime(23, 59, 59));

        waitingNumber++;

        // 웨이팅 생성
        Waiting waiting = new Waiting(store, party, requestDto.getWaitingType(), requestDto.getPersonnelCount(),
                WaitingStatus.PENDING, RemoteStatus.REMOTE, waitingNumber, user.getPhone());

        // 저장
        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingOnlineResponseDto(waitingNumber, savedWaiting);
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

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 현장 사용자로 저장
        OfflineUser offlineUser = new OfflineUser(requestDto.getPhoneNumber());
        offlineUserRepository.save(offlineUser);

        // 전화번호로 유저 가져오기
        Optional<User> user = userRepository.findByPhone(requestDto.getPhoneNumber());

        Party party = null;

        // 이미 존재하는 유저일 경우
        if(user.isPresent()) {
            party = partyRepository.save(new Party());

            PartyPeople partyPeople = new PartyPeople(party, user.get(), PartyRole.REPRESENTATIVE);

            // 일행 인원 등록
            partyPeopleRepository.save(partyPeople);
        }

        // 오늘 날짜 가져오기
        LocalDate date = LocalDate.now();

        // 웨이팅 번호
        int waitingNumber = waitingRepository.countByStoreAndCreatedAtBetween(store,
                date.atTime(0,0,0), date.atTime(23, 59, 59));

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
    public List<WaitingListResponseDto> getWaitingList(User user) {

        // 로그인 된 사용자로 PartyPeople 리스트 조회
        List<PartyPeople> partyPeopleList = partyPeopleRepository.findByUser(user);

        // PartyPeople 리스트에서 Waiting 추출
        List<Waiting> waitingList = partyPeopleList.stream()
                .map(PartyPeople::getParty)        // Party 추출
                .map(Party::getWaiting)           // Party에서 Waiting 추출
                .filter(Objects::nonNull)         // null 값 제외
                .toList();

        return waitingList.stream()
                .map(WaitingListResponseDto::new)
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
                .filter(checkUser -> checkUser.equals(user))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorCode.FORBIDDEN_PERMISSION));

        // 웨이팅 날짜만 가져오기
        LocalDate waitingDate = waiting.getCreatedAt().toLocalDate();

        // 웨이팅 번호
        int waitingNumber = waitingRepository.countByStoreAndCreatedAtBetween(waiting.getStore(),
                waitingDate.atTime(0,0,0), waitingDate.atTime(23, 59, 59));

        waitingNumber++;

        // 웨이팅 번호 변경
        waiting.updateWaitingNumber(waitingNumber);

        waitingRepository.save(waiting);

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
    public WaitingDetailResponseDto getWaitingDetail(Long waitingId, User user) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        // 사용자 권한이 일반 유저이면 해당 웨이팅에 권한이 있는지 확인
        if (user.getAuth() == Auth.USER) {
            waiting.getParty().getPartyPeople().stream()
                    .filter(partyPeople -> partyPeople.getUser().equals(user))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(ErrorCode.FORBIDDEN_PERMISSION));
        }

        return new WaitingDetailResponseDto(waiting);
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
                    .filter(checkUser -> checkUser.equals(user))
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
    public void changeWaitingStatus(Long waitingId, WaitingStatus waitingStatus) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        waiting.updateWaiting(waitingStatus);

        waitingRepository.save(waiting);
    }


    /**
     * 실시간 웨이팅 순서 조회 메서드
     *
     * @param waitingId
     * @return WaitingNowSeqNumberResponseDto
     */
    public WaitingNowSeqNumberResponseDto getNowSeqNumber(Long waitingId) {

        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        // 웨이팅 날짜만 가져오기
        LocalDate waitingDate = waiting.getCreatedAt().toLocalDate();

        // 앞에 대기중인 웨이팅 개수 가져오기
        int nowSeqNum = waitingRepository.countByStoreAndWaitingStatusAndCreatedAtBetweenAndWaitingNumberLessThanEqual(
                waiting.getStore(),
                WaitingStatus.PENDING,
                waitingDate.atTime(0,0,0), waitingDate.atTime(23, 59, 59),
                waiting.getWaitingNumber()
        );

        return new WaitingNowSeqNumberResponseDto(nowSeqNum);
    }
}
