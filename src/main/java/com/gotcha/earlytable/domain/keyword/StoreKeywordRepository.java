package com.gotcha.earlytable.domain.keyword;

import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreKeywordRepository extends JpaRepository<StoreKeyword, Long> {


    void deleteByKeywordKeywordIdAndStoreStoreIdIn(Long keywordId, List<Long> storeIds);
}
