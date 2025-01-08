package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.InterestStore;
import com.gotcha.earlytable.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestStoreRepository extends JpaRepository<InterestStore, Long> {

    List<Store> findByUserId(Long id);
}
