package com.gotcha.earlytable.domain.store.interestStore;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.file.FileDetailRepository;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.review.ReviewRepository;
import com.gotcha.earlytable.domain.review.enums.ReviewStatus;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.dto.InterestStoreResponseDto;
import com.gotcha.earlytable.domain.store.entity.InterestStore;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InterestStoreService {

    private final InterestStoreRepository interestStoreRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final FileDetailRepository fileDetailRepository;

    public InterestStoreService(InterestStoreRepository interestStoreRepository, StoreRepository storeRepository, MenuRepository menuRepository, ReviewRepository reviewRepository, FileDetailRepository fileDetailRepository) {

        this.interestStoreRepository = interestStoreRepository;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.fileDetailRepository = fileDetailRepository;
    }

    /**
     *  관심가게 등록
     * @param storeId
     * @param user
     */
    @Transactional
    public void registerStore(Long storeId, User user){

        //storeId에 해당하는 가게를 찾을 수 없는 경우 NOT FOUND
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        boolean isStore = interestStoreRepository.existsByStoreAndUser(store, user);
        if(isStore){throw new CustomException(ErrorCode.DUPLICATE_VALUE);}

        InterestStore interestStore = new InterestStore(user, store);
        interestStoreRepository.save(interestStore);
    }

    /**
     *  관심가게 조회
     * @param user
     * @return
     */
    public  List<InterestStoreResponseDto> getInterestStores(User user){

        // 유저가 등록한 관심가게를 일단 리스트로 불러와서 처리
        List<InterestStore> stores = interestStoreRepository.findAllByUserId(user.getId());
        List<InterestStoreResponseDto> responseDtoList = new ArrayList<>();
        for(InterestStore store : stores){
            Long storeId = store.getStore().getStoreId();
            System.out.println(storeId);
            String storeName = store.getStore().getStoreName();
            String storeContent = store.getStore().getStoreContents();
            StoreCategory storeCategory = store.getStore().getStoreCategory();
            Optional<Menu> optionalMenu = menuRepository.findByStoreStoreIdAndMenuStatus(store.getStore().getStoreId(), MenuStatus.RECOMMENDED);
            String presentMenu = optionalMenu.map(Menu::getMenuName)
                    .orElse("대표메뉴 없음");
            Double averageRating = reviewRepository.findAverageRatingByStore(store.getStore(), ReviewStatus.NORMAL);
            Long countReview = reviewRepository.countReviewsByStoreAndReviewStatus(store.getStore(), ReviewStatus.NORMAL);
            Optional<FileDetail> optionalFileDetail = fileDetailRepository.findByFileStoreStoreIdAndFileStatus(store.getStore().getStoreId(), FileStatus.REPRESENTATIVE);
            String storeImage = optionalFileDetail.map(FileDetail::getFileUrl)
                    .orElse("이미지 없음");
            InterestStoreResponseDto responseDto = new  InterestStoreResponseDto(storeId, storeName, storeContent, storeCategory, presentMenu, averageRating, countReview, storeImage);

            responseDtoList.add(responseDto);

        }
        return responseDtoList;
    }

    /**
     *  관심가게 삭제
     * @param storeId
     * @param user
     */
    @Transactional
    public void deleteStore(Long storeId, User user){

        // 가게가 있는지를 먼저 검증
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        //내 관심가게로 등록되어있는지 검증
        InterestStore interestStore = interestStoreRepository.findByStoreAndUserId(store, user.getId());
        if(interestStore == null){
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        interestStoreRepository.delete(interestStore);

    }

}
