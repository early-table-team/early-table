package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileRepository;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.keyword.StoreKeywordRepository;
import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.domain.reservation.ReservationRepository;
import com.gotcha.earlytable.domain.store.dto.*;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTable;
import com.gotcha.earlytable.domain.store.entity.StoreTimeSlot;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileDetailService fileDetailService;
    private final ReservationRepository reservationRepository;
    private final StoreKeywordRepository storeKeywordRepository;


    public StoreService(StoreRepository storeRepository, UserRepository userRepository,
                        FileRepository fileRepository, FileDetailService fileDetailService,
                        ReservationRepository reservationRepository,StoreKeywordRepository storeKeywordRepository) {


        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileDetailService = fileDetailService;
        this.reservationRepository = reservationRepository;
        this.storeKeywordRepository = storeKeywordRepository;
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

        // 가게 개수 제한 10개 이하 확인
        if (storeRepository.countStoreByUserId(requestDto.getUserId()) >= 10) {

            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        File file = fileRepository.save(new File());

        // 이미지 파일들 저장
        if(requestDto.getStoreImageList() != null && !requestDto.getStoreImageList().get(0).isEmpty()) {
            // 프로필 이미지 파일 저장
            fileDetailService.createImageFiles(requestDto.getStoreImageList(), file);
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
     * @return StoreResponseDto
     */
    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreUpdateRequestDto requestDto) {

        // 가게 정보 가져오기
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 가게 내용 변경 및 저장
        store.updateStore(requestDto);
        storeRepository.save(store);

        // 이미지 수정
        if(requestDto.getFileUrlList() != null && !requestDto.getFileUrlList().isEmpty()) {

            fileDetailService.updateFileDetail(requestDto.getNewStoreImageList(), requestDto.getFileUrlList(), store.getFile());
        }

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

        // 이미지가 변경되었는지 확인
        if(!store.getFile().getFileId().equals(pendingStore.getFileId())) {
            // 파일 삭제
            fileRepository.deleteById(store.getFile().getFileId());
        }

        // 파일 정보 가져오기
        File file = fileRepository.findByIdOrElseThrow(pendingStore.getFileId());

        // 가게 내용 변경
        store.updateStoreFromPendingStore(pendingStore, file);

        // 수정된 가게 정보 저장
        storeRepository.save(store);
    }

    /**
     * 가게 단건 조회 메서드
     *
     * @param storeId
     * @return StoreResponseDto
     */
    public StoreResponseDto getStore(Long storeId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        return StoreResponseDto.toDto(store);
    }

    /**
     * 나의 가게 전체 조회 메서드
     *
     * @param userId
     * @return List<StoreListResponseDto>
     */
    public List<StoreListResponseDto> getStores(Long userId) {

        List<Store> storeList = storeRepository.findAllByUserId(userId);

        return storeList.stream().map(StoreListResponseDto::toDto).toList();
    }

    /**
     * 나의 가게 휴업 상태<-> 영업상태로 변경 메서드
     *
     * @param storeId
     * @param userId
     * @return String
     */
    @Transactional
    public String updateStoreStatus(Long storeId, Long userId) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 나의 가게에 접근하는지 확인
        if (!store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        String message = "현재 상태를 변경할 수 없습니다. 관리자에게 문의하세요.";

        // 영업 상태로 변경
        if (store.getStoreStatus().equals(StoreStatus.RESTING)) {
            store.updateStoreStatus(StoreStatus.APPROVED);
            message = "정상 영업 상태로 변경되었습니다.";
        }
        // 휴업 상태로 변경
        else if (store.getStoreStatus().equals(StoreStatus.APPROVED)) {
            store.updateStoreStatus(StoreStatus.RESTING);
            message = "휴업 상태로 변경되었습니다.";
        }

        storeRepository.save(store);

        return message;
    }

    /**
     * 가게 상태 변경 메서드 (Admin)
     *
     * @param storeId
     * @param storeStatus
     */
    @Transactional
    public void updateStoreStatus(Long storeId, StoreStatus storeStatus) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        store.updateStoreStatus(storeStatus);

        storeRepository.save(store);

    }

    /**
     * 가게 조건 검색 메서드
     *
     * @param requestDto
     * @return
     */
    public List<StoreSearchResponseDto> searchStore(StoreSearchRequestDto requestDto) {

        return storeRepository.searchStoreQuery(requestDto);
    }

    /**
     * 가게 모든 타임, 모든 테이블의 잔여 개수 가져오기 메서드
     *
     * @param storeId
     * @param date
     * @return
     */
    public List<StoreReservationTotalDto> getStoreReservationTotal(Long storeId, LocalDate date) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 현재 시간
        LocalTime now = LocalTime.now();

        List<StoreReservationTotalDto> totalDtoList = new ArrayList<>();

        // 모든 시간에
        for(StoreTimeSlot storeTimeSlot : store.getStoreTimeSlotList()) {

            // 현재 시간보다 전에 있던 내역은 제외
            if(storeTimeSlot.getReservationTime().isBefore(now)) {
                continue;
            }

            // 모든 테이블에
            for(StoreTable storeTable : store.getStoreTableList()) {
                // 예약 타임
                LocalTime reservationTime = storeTimeSlot.getReservationTime();
                // 최대 수용 가능 인원
                int tableMaxNumber = storeTable.getTableMaxNumber();
                // 최소 수용 인원
                int tableMinNumber = tableMaxNumber - 1;
                // 잔여 개수
                int remainTableCount = storeTable.getTableCount() - reservationRepository
                        .countByReservationDateAndReservationTimeAndTableSizeAndReservationStatusNot(
                                date, reservationTime, tableMaxNumber, ReservationStatus.CANCELED);

                totalDtoList.add(new StoreReservationTotalDto(reservationTime, tableMaxNumber, tableMinNumber, remainTableCount));
            }
        }

        return totalDtoList;
    }

    /**
     *  키워드로 가게 검색
     * @param keyword
     * @return
     */
    public List<StoreSearchResponseDto> searchKeywordStore(String keyword) {

        // 스토어 키워드가 일치하는 것들을 리스트 타입으로 가져옴
        List<StoreKeyword> storeKeyword = storeKeywordRepository.findAllByKeywordKeyword(keyword);


        // 해당 가게들로 DTO를 생성 후 반환
        List<StoreSearchResponseDto> dtos = new ArrayList<>();
        for(StoreKeyword storeKeywordDto : storeKeyword) {
            StoreSearchResponseDto dto = new StoreSearchResponseDto(storeKeywordDto.getStore());
            dtos.add(dto);
        }

        return dtos;

    }
}
