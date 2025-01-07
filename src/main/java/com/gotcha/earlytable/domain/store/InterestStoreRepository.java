package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.InterestStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestStoreRepository extends JpaRepository<InterestStore, Long> {
}
