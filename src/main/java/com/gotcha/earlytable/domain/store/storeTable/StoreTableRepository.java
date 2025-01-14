package com.gotcha.earlytable.domain.store.storeTable;

import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.entity.StoreTable;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreTableRepository extends JpaRepository<StoreTable, Long> {


    boolean existsByStoreAndTableMaxNumber(Store store, Integer tableMaxNumber);

    StoreTable findByStoreAndTableMaxNumber(Store store, Integer tableMaxNumber);

    List<StoreTable> findAllByStore(Store store);

    default StoreTable findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }
}
