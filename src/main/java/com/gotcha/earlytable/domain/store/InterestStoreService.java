package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.file.FileRepository;
import com.gotcha.earlytable.domain.file.FileStatus;
import com.gotcha.earlytable.domain.file.ImageFileRepository;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.ImageFile;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.review.ReviewRepository;
import com.gotcha.earlytable.domain.store.dto.InterestStoreListResponseDto;
import com.gotcha.earlytable.domain.store.dto.InterestStoreResponseDto;
import com.gotcha.earlytable.domain.store.entity.InterestStore;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterestStoreService {

    private final InterestStoreRepository interestStoreRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final ImageFileRepository imageFileRepository;

    public InterestStoreService(InterestStoreRepository interestStoreRepository, StoreRepository storeRepository, MenuRepository menuRepository, ReviewRepository reviewRepository, ImageFileRepository imageFileRepository) {

        this.interestStoreRepository = interestStoreRepository;
        this.storeRepository = storeRepository;
        this.menuRepository = menuRepository;
        this.reviewRepository = reviewRepository;
        this.imageFileRepository = imageFileRepository;
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
        List<Store> stores = interestStoreRepository.findByUserId(user.getId());
        List<InterestStoreResponseDto> responseDtoList = new ArrayList<>();
        for(Store store : stores){
            Long storeId = store.getStoreId();
            String storeName = store.getStoreName();
            String storeContent = store.getStoreContents();
            StoreCategory storeCategory = store.getStoreCategory();
            String presentMenu = menuRepository.findByStoreStoreIdAndMenuStatus(store.getStoreId(), MenuStatus.RECOMMENDED).getMenuName();
            Double averageRating = reviewRepository.findAverageRatingByStore(store);
            Long countReview = reviewRepository.countReviewsByStore(store);
            String storeImage = imageFileRepository.findByFileStoreStoreIdAndFileStatus(store.getStoreId(), FileStatus.REPRESENTATIVE).getFileUrl();

            InterestStoreResponseDto responseDto = new  InterestStoreResponseDto(storeId, storeName, storeContent, storeCategory, presentMenu, averageRating, countReview, storeImage);

            responseDtoList.add(responseDto);

        }
        return responseDtoList;
    }


}
