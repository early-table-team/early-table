package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.party.PartyPeopleRepository;
import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.party.entity.PartyPeople;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.domain.waiting.dto.*;
import com.gotcha.earlytable.domain.waiting.entity.OfflineUser;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.domain.waiting.entity.WaitingNumber;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final PartyRepository partyRepository;
    private final OfflineUserRepository offlineUserRepository;
    private final PartyPeopleRepository partyPeopleRepository;
    private final WaitingNumberRepository waitingNumberRepository;

    public WaitingService(WaitingRepository waitingRepository, StoreRepository storeRepository, PartyRepository partyRepository, OfflineUserRepository offlineUserRepository, PartyPeopleRepository partyPeopleRepository, WaitingNumberRepository waitingNumberRepository) {
        this.waitingRepository = waitingRepository;
        this.storeRepository = storeRepository;
        this.partyRepository = partyRepository;
        this.offlineUserRepository = offlineUserRepository;
        this.partyPeopleRepository = partyPeopleRepository;
        this.waitingNumberRepository = waitingNumberRepository;
    }

    /**
     * 원격 웨이팅 생성 메서드
     *
     * @param requestDto
     * @param storeId
     * @return
     */
    @Transactional
    public WaitingOnlineResponseDto createWaitingOnline(@Valid WaitingOnlineRequestDto requestDto, Long storeId, User user) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        Party party = partyRepository.save(new Party());
        PartyPeople partyPeople = new PartyPeople(party, user, PartyRole.REPRESENTATIVE);
        partyPeopleRepository.save(partyPeople);

        WaitingNumber waitingNumber = new WaitingNumber(waitingRepository.countByStoreAndWaitingType(store, requestDto.getWaitingType()));
        WaitingNumber savedwaitingNumber = waitingNumberRepository.save(waitingNumber);

        Waiting waiting = new Waiting(store, party, requestDto.getWaitingType(), requestDto.getPersonnelCount(), WaitingStatus.PENDING, savedwaitingNumber);
        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingOnlineResponseDto(savedWaiting);
    }

    /**
     * 현장 웨이팅 생성 메서드
     *
     * @param requestDto
     * @param storeId
     * @return
     */
    @Transactional
    public WaitingNumberResponseDto createWaitingOffline(@Valid WaitingOfflineRequestDto requestDto, Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        OfflineUser offlineUser = new OfflineUser(requestDto.getPhoneNumber());
        OfflineUser savedOfflineUser = offlineUserRepository.save(offlineUser);

        WaitingNumber waitingNumber = new WaitingNumber(waitingRepository.countByStoreAndWaitingType(store, requestDto.getWaitingType()));
        WaitingNumber savedwaitingNumber = waitingNumberRepository.save(waitingNumber);

        Waiting waiting = new Waiting(store, savedOfflineUser, requestDto.getWaitingType(), requestDto.getPersonnelCount(), WaitingStatus.PENDING, savedwaitingNumber);
        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingNumberResponseDto(savedWaiting);
    }

    /**
     * 웨이팅 목록 조회 메서드
     *
     * @param user
     * @return
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

        WaitingNumber waitingNumber = waiting.getWaitingNumber();
        waitingNumber.updateWaitingNumber(waitingRepository.countByStoreAndWaitingType(waiting.getStore(), waiting.getWaitingType()));
        waitingNumberRepository.save(waitingNumber);

        return new WaitingNumberResponseDto(waiting);
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void resetWaitingNumber() {
        waitingNumberRepository.deleteAll();
    }
}
