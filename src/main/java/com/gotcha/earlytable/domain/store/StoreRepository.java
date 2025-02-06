package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryQuery {

    default Store findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    int countStoreByUserId(Long userId);

    List<Store> findAllByUserId(Long userId);


    boolean existsByStoreIdAndUserId(Long storeId, Long id);

    List<Store> findAllByStoreIdIn(List<Long> storeIds);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Store s WHERE s.storeId = :storeId")
    Optional<Store> findByIdWithLock(Long storeId);

}
