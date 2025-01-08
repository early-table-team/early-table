package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.file.FileRepository;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.store.dto.StoreRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreResponseDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public StoreService(StoreRepository storeRepository, UserRepository userRepository, FileRepository fileRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * 가게 생성 메서드 (Admin)
     *
     * @param requestDto
     * @return StoreResponseDto
     */
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto requestDto) {

        // 필요한 객체를 가져오기
        User user = userRepository.findByIdOrElseThrow(requestDto.getUserId());
        File file = fileRepository.findByIdOrElseThrow(requestDto.getFileId());

        // 가게 개수 제한 10개 이하 확인
        if (storeRepository.countStoreByUserId(requestDto.getUserId()) >= 10) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }


        // 가게 객체 생성
        Store store = new Store(requestDto.getStoreName(), requestDto.getStoreTel(),
                requestDto.getStoreContents(), requestDto.getStoreAddress(),
                StoreStatus.PENDING, requestDto.getStoreCategory(),
                requestDto.getRegionTop(), requestDto.getRegionBottom(),
                user, file
        );

        // 가게 저장
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.toDto(savedStore);
    }

    /**
     * 가게 생성 메서드 from PendingStore
     *
     * @param pendingStore
     */
    @Transactional
    public void createStoreFromPendingStore(PendingStore pendingStore) {

        // 필요한 객체를 가져오기
        File file = fileRepository.findByIdOrElseThrow(pendingStore.getFileId());

        // 가게 개수 제한 10개 이하 확인
        if (storeRepository.countStoreByUserId(pendingStore.getUser().getId()) >= 10) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 가게 객체 생성
        Store store = new Store(pendingStore.getStoreName(), pendingStore.getStoreTel(),
                pendingStore.getStoreContents(), pendingStore.getStoreAddress(),
                StoreStatus.APPROVED, pendingStore.getStoreCategory(),
                pendingStore.getRegionTop(), pendingStore.getRegionBottom(),
                pendingStore.getUser(), file
        );

        // 가게 저장
        storeRepository.save(store);
    }

    /**
     * 가게 수정 메서드 (Admin)
     *
     * @param storeId
     * @param requestDto
     * @return StoreResponseDt
     */
    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreRequestDto requestDto) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 가게 내용 변경
        store.updateStore(requestDto);

        // 수정된 가게 정보 저장
        storeRepository.save(store);

        return StoreResponseDto.toDto(store);

    }

    /**
     * 가게 수정 메서드 from PendingStore
     *
     * @param pendingStore
     */
    @Transactional
    public void updateStoreFromPendingStore(PendingStore pendingStore) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(pendingStore.getStoreId());

        // 파일 정보 가져오기
        File file = fileRepository.findByIdOrElseThrow(pendingStore.getFileId());

        // 가게 내용 변경
        store.updateStoreFromPendingStore(pendingStore, file);

        // 수정된 가게 정보 저장
        storeRepository.save(store);
    }

    @Transactional
    public void updateStoreStatus(Long storeId, StoreStatus storeStatus) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        store.updateStoreStatus(storeStatus);

        storeRepository.save(store);
    }
}
