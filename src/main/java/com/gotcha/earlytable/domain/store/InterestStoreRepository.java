package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.InterestStore;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestStoreRepository extends JpaRepository<InterestStore, Long> {


    InterestStore findByStoreAndUserId(Store store, Long userId);

}
