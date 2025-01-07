package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.party.entity.Party;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.waiting.dto.WaitingRequestDto;
import com.gotcha.earlytable.domain.waiting.dto.WaitingResponseDto;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    StoreRepository storeRepository;

    public WaitingService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    /**
     * 웨이팅 생성 API
     *
     * @param requestDto
     * @param storeId
     * @return
     */
    public WaitingResponseDto creatWaiting(@Valid WaitingRequestDto requestDto, Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        Waiting waiting = new Waiting(store, new Party(), requestDto.getWaitingType(), requestDto.getPersonnelCount(), WaitingStatus.PENDING);

        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingResponseDto(savedWaiting);
    }
}
