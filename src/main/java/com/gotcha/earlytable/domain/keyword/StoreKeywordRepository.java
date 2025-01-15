package com.gotcha.earlytable.domain.keyword;

import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import com.gotcha.earlytable.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreKeywordRepository extends JpaRepository<StoreKeyword, Long> {


    Integer deleteByKeywordKeywordIdAndStoreStoreIdIn(Long keywordId, List<Long> storeIds);

    boolean existsByStore(Store store);

    @Query("SELECT sk FROM StoreKeyword sk JOIN sk.keyword k WHERE k.keyword = :keyword")
    List<StoreKeyword> findAllByKeyword_Keyword(@Param("keyword") String keyword);
}
