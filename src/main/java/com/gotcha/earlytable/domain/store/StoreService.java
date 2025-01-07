package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.store.dto.StoreCreateRequestDto;
import com.gotcha.earlytable.domain.store.dto.StoreResponseDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final FileService fileService;

    public StoreService(StoreRepository storeRepository, FileService fileService) {
        this.storeRepository = storeRepository;
        this.fileService = fileService;
    }

    /**
     * 가게 생성 메서드
     *
     * @param user
     * @param requestDto
     * @return StoreResponseDto
     */
    @Transactional
    public StoreResponseDto createStore(User user, StoreCreateRequestDto requestDto) {

        // 가게 개수 제한 10개 이하 확인
        if(storeRepository.countStoreByUserId(user.getId()) >= 10){
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // 파일 객체 생성
        File file = fileService.createFile();

        // 가게 객체 생성
        Store store = new Store(requestDto.getStoreName(), requestDto.getStoreTel(),
                requestDto.getStoreContents(), requestDto.getStoreAddress(),
                StoreStatus.PENDING, requestDto.getStorecategory(),
                requestDto.getRegionTop(), requestDto.getRegionBottom(),
                user, file
        );

        // 가게 저장
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.toDto(savedStore);
    }
}
