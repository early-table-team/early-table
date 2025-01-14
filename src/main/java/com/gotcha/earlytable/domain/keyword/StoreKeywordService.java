package com.gotcha.earlytable.domain.keyword;

import com.gotcha.earlytable.domain.keyword.dto.StoreKeywordRequestDto;
import com.gotcha.earlytable.domain.keyword.entity.Keyword;
import com.gotcha.earlytable.domain.keyword.entity.StoreKeyword;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreKeywordService {

    private final StoreKeywordRepository storeKeywordRepository;
    private final StoreRepository storeRepository;
    private final KeywordRepository keywordRepository;

    public StoreKeywordService(StoreKeywordRepository storeKeywordRepository, StoreRepository storeRepository,
                               KeywordRepository keywordRepository) {
        this.storeKeywordRepository = storeKeywordRepository;
        this.storeRepository = storeRepository;
        this.keywordRepository = keywordRepository;
    }

    /**
     * 가게키워드 생성 메서드
     *
     * @param requestDto
     */
    @Transactional
    public void createStoreKeywords(StoreKeywordRequestDto requestDto) {

        Keyword keyword = keywordRepository.findByIdOrElseThrow(requestDto.getKeywordId());
        List<Store> storeList = storeRepository.findAllByStoreIdIn(requestDto.getStoreIds());

        // 등록할 가게가 비어있으면 리턴
        if(storeList.isEmpty()) {
            return;
        }

        List<StoreKeyword> storeKeywordList = new ArrayList<>();

        // 가게 키워드 리스트 생성
        for(Store store  : storeList) {
            // 존재하면 패스
            if(storeKeywordRepository.existsByStore(store)){
                continue;
            }
            StoreKeyword storeKeyword = new StoreKeyword(store, keyword);
            storeKeywordList.add(storeKeyword);
        }

        // 저장
        storeKeywordRepository.saveAll(storeKeywordList);
    }

    /**
     * 가게키워드 삭제 메서드
     *
     * @param requestDto
     */
    @Transactional
    public void deleteStoreKeywords(StoreKeywordRequestDto requestDto) {

        if(requestDto.getKeywordId() == null || requestDto.getStoreIds().isEmpty()) {
            return;
        }

        // 삭제
        Integer countDelete = storeKeywordRepository.deleteByKeywordKeywordIdAndStoreStoreIdIn(requestDto.getKeywordId(),
                                                                            requestDto.getStoreIds());

        // 하나라도 제거를 못하면 예외처리
        if(countDelete == 0) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
    }
}
