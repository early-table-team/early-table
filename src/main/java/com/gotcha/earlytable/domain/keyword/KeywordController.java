package com.gotcha.earlytable.domain.keyword;

import com.gotcha.earlytable.domain.keyword.dto.KeywordRequestDto;
import com.gotcha.earlytable.domain.keyword.dto.KeywordResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController {

    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    /**
     * 키워드 등록 API
     *
     * @param keywordRequestDto
     * @return ResponseEntity<KeywordResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PostMapping
    public ResponseEntity<KeywordResponseDto> createKeyword(@Valid @RequestBody KeywordRequestDto keywordRequestDto) {

        KeywordResponseDto responseDto = keywordService.createKeyword(keywordRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 키워드 수정 API
     *
     * @param keywordId
     * @param keywordRequestDto
     * @return ResponseEntity<KeywordResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PatchMapping("/{keywordId}")
    public ResponseEntity<KeywordResponseDto> updateKeyword(@PathVariable Long keywordId,
                                                            @Valid @RequestBody KeywordRequestDto keywordRequestDto) {

        KeywordResponseDto responseDto = keywordService.updateKeyword(keywordId, keywordRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 키워드 전체 조회 API
     *
     * @return ResponseEntity<List<KeywordResponseDto>>
     */
    @GetMapping
    public ResponseEntity<List<KeywordResponseDto>> getAllKeywords() {

        List<KeywordResponseDto> responseDtoList = keywordService.getKeywords();

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    /**
     * 키워드 단건 조회 API
     *
     * @param keywordId
     * @return ResponseEntity<KeywordResponseDto>
     */
    @GetMapping("/{keywordId}")
    public ResponseEntity<KeywordResponseDto> getKeyword(@PathVariable Long keywordId) {

        KeywordResponseDto responseDto = keywordService.getKeyword(keywordId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 키워드 삭제 API
     *
     * @param keywordId
     * @return ResponseEntity<Void>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Long keywordId) {

        keywordService.deleteKeyword(keywordId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
