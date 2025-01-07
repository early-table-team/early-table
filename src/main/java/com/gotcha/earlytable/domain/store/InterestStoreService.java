package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.InterestStore;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterestStoreService {

    private final InterestStoreRepository interestStoreRepository;
    private final StoreRepository storeRepository;

    public InterestStoreService(InterestStoreRepository interestStoreRepository, StoreRepository storeRepository) {

        this.interestStoreRepository = interestStoreRepository;
        this.storeRepository = storeRepository;
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


}
