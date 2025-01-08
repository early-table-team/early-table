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
import com.gotcha.earlytable.global.enums.WaitingStatus;
import jakarta.validation.Valid;
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

    public WaitingService(WaitingRepository waitingRepository, StoreRepository storeRepository, PartyRepository partyRepository, OfflineUserRepository offlineUserRepository, PartyPeopleRepository partyPeopleRepository) {
        this.waitingRepository = waitingRepository;
        this.storeRepository = storeRepository;
        this.partyRepository = partyRepository;
        this.offlineUserRepository = offlineUserRepository;
        this.partyPeopleRepository = partyPeopleRepository;
    }

    /**
     * 원격 웨이팅 생성 메서드
     *
     * @param requestDto
     * @param storeId
     * @return
     */
    @Transactional
    public WaitingOnlineResponseDto creatWaitingOnline(@Valid WaitingOnlineRequestDto requestDto, Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        Party party = partyRepository.save(new Party());

        Waiting waiting = new Waiting(store, party, requestDto.getWaitingType(), requestDto.getPersonnelCount(), WaitingStatus.PENDING);

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
    public WaitingOfflineResponseDto creatWaitingOffline(@Valid WaitingOfflineRequestDto requestDto, Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        OfflineUser offlineUser = new OfflineUser(requestDto.getPhoneNumber());

        OfflineUser savedOfflineUser = offlineUserRepository.save(offlineUser);

        Waiting waiting = new Waiting(store, savedOfflineUser, requestDto.getWaitingType(), requestDto.getPersonnelCount(), WaitingStatus.PENDING);

        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingOfflineResponseDto(savedWaiting);
    }

    /**
     *  웨이팅 목록 조회 메서드
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
}
