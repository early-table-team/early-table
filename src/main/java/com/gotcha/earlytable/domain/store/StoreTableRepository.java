package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {


    boolean existsByStoreAndTableMaxNumber(Store store, Integer tableMaxNumber);

}
