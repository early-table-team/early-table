package com.gotcha.earlytable.domain.keyword;

import com.gotcha.earlytable.domain.keyword.dto.KeywordRequestDto;
import com.gotcha.earlytable.domain.keyword.dto.KeywordResponseDto;
import com.gotcha.earlytable.domain.keyword.entity.Keyword;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    /**
     * 키워드 등록 메서드
     *
     * @param keywordRequestDto
     * @return KeywordResponseDto
     */
    @Transactional
    public KeywordResponseDto createKeyword(KeywordRequestDto keywordRequestDto) {

        // 중복 여부 체크
        if(keywordRepository.existsByKeyword(keywordRequestDto.getKeyword())) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        // 키워드 생성
        Keyword keyword = new Keyword(keywordRequestDto.getKeyword());

        // 저장
        Keyword savedKeyword =  keywordRepository.save(keyword);

        return KeywordResponseDto.toDto(savedKeyword);
    }

    /**
     * 키워드 수정 메서드
     *
     * @param keywordId
     * @param requestDto
     * @return KeywordResponseDto
     */
    public KeywordResponseDto updateKeyword(Long keywordId, KeywordRequestDto requestDto) {

        // 중복 여부 체크
        if(keywordRepository.existsByKeyword(requestDto.getKeyword())) {
            throw new ConflictException(ErrorCode.DUPLICATE_VALUE);
        }

        Keyword keyword = keywordRepository.findByIdOrElseThrow(keywordId);

        // 키워드 수정
        keyword.updateKeyword(requestDto);

        // 저장
        keywordRepository.save(keyword);

        return KeywordResponseDto.toDto(keyword);
    }

    /**
     * 키워드 전체 조회 메서드
     *
     * @return List<KeywordResponseDto>
     */
    public List<KeywordResponseDto> getKeywords() {

        // 키워드 리스트 가져오기
        List<Keyword> keywordList = keywordRepository.findAll();

        return keywordList.stream().map(KeywordResponseDto::toDto).toList();
    }

    /**
     * 키워드 단건 조회 메서드
     *
     * @param keywordId
     * @return KeywordResponseDto
     */
    public KeywordResponseDto getKeyword(Long keywordId) {

        // 조회
        Keyword keyword = keywordRepository.findByIdOrElseThrow(keywordId);

        return KeywordResponseDto.toDto(keyword);
    }

    /**
     * 키워드 삭제 메서드
     *
     * @param keywordId
     */
    public void deleteKeyword(Long keywordId) {

        // 키워드 삭제
        keywordRepository.deleteById(keywordId);
    }
}
