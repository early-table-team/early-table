package com.gotcha.earlytable.domain.store.interestStore;

import com.gotcha.earlytable.domain.store.entity.InterestStore;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestStoreRepository extends JpaRepository<InterestStore, Long> {

    InterestStore findByStoreAndUserId(Store store, Long userId);

    List<InterestStore> findAllByUserId(Long id);

    boolean existsByStoreAndUser(Store store, User user);

    void deleteByStore(Store store);
}
