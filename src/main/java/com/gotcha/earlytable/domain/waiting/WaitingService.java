package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.party.PartyRepository;
import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waiting.dto.WaitingOfflineRequestDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingOfflineResponseDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingOnlineRequestDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingOnlineResponseDto;
import com.gotcha.earlytable.domain.waiting.entity.OfflineUser;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    StoreRepository storeRepository;
    private final PartyRepository partyRepository;

    public WaitingService(WaitingRepository waitingRepository, PartyRepository partyRepository) {
        this.waitingRepository = waitingRepository;
        this.partyRepository = partyRepository;
    }

    /**
     * 웨이팅 생성 API
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


    @Transactional
    public WaitingOfflineResponseDto creatWaitingOffline(@Valid WaitingOfflineRequestDto requestDto, Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        OfflineUser offlineUser = new OfflineUser(requestDto.getPhoneNumber());

        Waiting waiting = new Waiting(store, offlineUser, requestDto.getWaitingType(), requestDto.getPersonnelCount(), WaitingStatus.PENDING);

        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingOfflineResponseDto(savedWaiting);
    }
}
