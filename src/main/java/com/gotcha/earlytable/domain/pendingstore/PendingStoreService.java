package com.gotcha.earlytable.domain.pendingstore;

import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.pendingstore.dto.*;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.pendingstore.enums.PendingStoreStatus;
import com.gotcha.earlytable.domain.pendingstore.enums.PendingStoreType;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.StoreService;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PendingStoreService {

    private final PendingStoreRepository pendingStoreRepository;
    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final FileDetailService fileDetailService;
    private final StoreService storeService;

    public PendingStoreService(PendingStoreRepository pendingStoreRepository, StoreRepository storeRepository,
                               FileService fileService, FileDetailService fileDetailService, StoreService storeService) {
        this.pendingStoreRepository = pendingStoreRepository;
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.fileDetailService = fileDetailService;
        this.storeService = storeService;
    }

    /**
     * 가게 생성 요청 메서드
     *
     * @param requestDto
     * @return StoreResponseDto
     */
    @Transactional
    public PendingStoreResponseDto createPendingStore(User user, PendingStoreRequestDto requestDto) {

        // 파일 객체 생성
        File file = fileService.createFile();

        if(requestDto.getStoreImageList() != null && !requestDto.getStoreImageList().get(0).isEmpty()) {
            // 이미지 파일들 저장
            fileDetailService.createImageFiles(requestDto.getStoreImageList(), file);
        }

        // 팬딩 가게 객체 생성
        PendingStore pendingStore = new PendingStore(user, requestDto.getStoreName(), requestDto.getStoreTel(),
                requestDto.getStoreContents(), requestDto.getStoreAddress(),
                StoreStatus.PENDING, requestDto.getStoreCategory(),
                requestDto.getRegionTop(), requestDto.getRegionBottom(),
                file.getFileId(), null, PendingStoreType.CREATE
        );

        // 팬딩 가게 저장
        PendingStore savedPendingStore = pendingStoreRepository.save(pendingStore);

        return PendingStoreResponseDto.toDto(savedPendingStore, file);
    }

    /**
     * 가게 수정 요청 메서드
     *
     * @param storeId
     * @param requestDto
     * @return PendingStoreResponseDto
     */
    @Transactional
    public PendingStoreResponseDto updatePendingStore(Long storeId, User user,  PendingStoreUpdateRequestDto requestDto) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 본인 가게가 아니면 수정 요청 불가
        if(!store.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        File file = store.getFile();

        if(requestDto.getFileUrlList() != null && !requestDto.getFileUrlList().isEmpty()) {

            file = fileService.createFile();

            // 새로운 파일에 파일 디테일 복사
            fileDetailService.copyFileDetails(store.getFile().getFileDetailList(), file);

            // 이미지 수정
            fileDetailService.updateFileDetail(requestDto.getNewStoreImageList(), requestDto.getFileUrlList(), file);
        }

        // 가게 객체 생성
        PendingStore pendingStore = new PendingStore(user, requestDto.getStoreName(), requestDto.getStoreTel(),
                requestDto.getStoreContents(), requestDto.getStoreAddress(),
                StoreStatus.PENDING, requestDto.getStoreCategory(),
                requestDto.getRegionTop(), requestDto.getRegionBottom(),
                file.getFileId(), storeId, PendingStoreType.UPDATE
        );


        // 팬딩 가게 저장
        PendingStore savedPendingStore = pendingStoreRepository.save(pendingStore);

        return PendingStoreResponseDto.toDto(savedPendingStore, file);
    }

    /**
     * 팬딩 가게 전체 목록 조회 메서드
     *
     * @param pageable
     * @return List<PendingStoreResponseListDto>
     */
    public List<PendingStoreResponseListDto> getPendingStores(Pageable pageable) {

        List<PendingStore> pendingStores = pendingStoreRepository.findAllByOrderByCreatedAtDesc(pageable);

        return pendingStores.stream().map(PendingStoreResponseListDto::toDto).toList();
    }

    /**
     * 팬딩 가게 단건 조회 메서드
     *
     * @param pendingStoreId
     * @return PendingStoreResponseDto
     */
    public PendingStoreResponseDto getPendingStore(Long pendingStoreId) {

        // 팬딩 가게 정보 가져오기
        PendingStore pendingStore = pendingStoreRepository.findByIdOrElseThrow(pendingStoreId);

        // 가게 파일 정보 가져오기
        File file = fileService.getFile(pendingStore.getFileId());

        return PendingStoreResponseDto.toDto(pendingStore, file);
    }

    /**
     * 팬딩 가게에 대한 어드민 결정 메서드
     *
     * @param pendingStoreId
     * @param requestDto
     */
    @Transactional
    public void updatePendingStoreStatus(Long pendingStoreId, PendingStoreStatusRequestDto requestDto) {

        // 팬딩 가게 정보 가져오기
        PendingStore pendingStore = pendingStoreRepository.findByIdOrElseThrow(pendingStoreId);

        // 거절시
        if(requestDto.getPendingStoreStatus() == PendingStoreStatus.REJECT) {
            // 상태 거절 변경
            pendingStore.updateStoreStatus(StoreStatus.REJECTED);

            return;
        }

        // 승인시
        if(pendingStore.getPendingStoreType() == PendingStoreType.CREATE) {
            // 진짜 가게 생성
            storeService.createStoreFromPendingStore(pendingStore);

        }
        else if(pendingStore.getPendingStoreType() == PendingStoreType.UPDATE) {
            // 가게 정보 수정
            storeService.updateStoreFromPendingStore(pendingStore);
        }
        else {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 상태 승인 변경
        pendingStore.updateStoreStatus(StoreStatus.APPROVED);
    }

    /**
     * 나의 가게 요청 전체 목록 조회 메서드
     *
     * @param pageable
     * @param user
     * @return List<PendingStoreResponseListDto>
     */
    public List<PendingStoreResponseListDto> getPendingStoresForOwner(Pageable pageable, User user) {

        List<PendingStore> pendingStores = pendingStoreRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

        return pendingStores.stream().map(PendingStoreResponseListDto::toDto).toList();
    }

    /**
     * 나의 가게 요청 단건 조회 메서드
     *
     * @param pendingStoreId
     * @param user
     * @return PendingStoreResponseDto
     */
    public PendingStoreResponseDto getPendingStoreForOwner(Long pendingStoreId, User user) {

        // 팬딩 가게 정보 가져오기
        PendingStore pendingStore = pendingStoreRepository.findByIdOrElseThrow(pendingStoreId);

        // 본인 가게 아니면 조회 불가능
        if(!pendingStore.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // 가게 파일 정보 가져오기
        File file = fileService.getFile(pendingStore.getFileId());

        return PendingStoreResponseDto.toDto(pendingStore, file);


    }
}
