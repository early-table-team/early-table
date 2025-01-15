package com.gotcha.earlytable.domain.pendingstore;

import com.gotcha.earlytable.domain.pendingstore.entity.PendingStore;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingStoreRepository extends JpaRepository<PendingStore, Long> {

    default PendingStore findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    List<PendingStore> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<PendingStore> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
