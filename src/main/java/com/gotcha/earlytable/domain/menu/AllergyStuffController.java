package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.AllergyStuffRequestDto;
import com.gotcha.earlytable.domain.menu.dto.AllergyStuffResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allergies/{categoryId}")
public class AllergyStuffController {

    private final AllergyStuffService allergyStuffService;

    public AllergyStuffController(AllergyStuffService allergyStuffService) {
        this.allergyStuffService = allergyStuffService;
    }

    /**
     * 알러지 원재료 등록 API
     * @param categoryId
     * @param allergyStuffRequestDto
     * @return AllergyStuffResponseDto
     */
    @PostMapping("/allergyStuff")
    public ResponseEntity<AllergyStuffResponseDto> createAllergyStuff(@PathVariable Long categoryId,
                                                                      @ModelAttribute AllergyStuffRequestDto allergyStuffRequestDto) {
        AllergyStuffResponseDto createAllergyStuffResponseDto = allergyStuffService.createAllergyStuff(categoryId, allergyStuffRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createAllergyStuffResponseDto);
    }

    /**
     * 알러지 원재료 수정 API
     * @param categoryId
     * @param stuffId
     * @param allergyStuffRequestDto
     * @return AllergyStuffResponseDto
     */
    @PutMapping("/allergyStuff/{stuffId}")
    public ResponseEntity<AllergyStuffResponseDto> updateAllergyStuff(@PathVariable Long categoryId, @PathVariable Long stuffId,
                                                                      @ModelAttribute AllergyStuffRequestDto allergyStuffRequestDto) {
        AllergyStuffResponseDto updateAllergyStuffResponseDto = allergyStuffService.updateAllergyStuff(categoryId, stuffId, allergyStuffRequestDto);

        return  ResponseEntity.status(HttpStatus.OK).body(updateAllergyStuffResponseDto);
    }

    /**
     * 카테고리 기준 알러지 원재료 조회 API
     * @param categoryId
     * @return List<AllergyStuffResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<AllergyStuffResponseDto>> getAllergyStuffListByCategory(@PathVariable Long categoryId) {
        List<AllergyStuffResponseDto> allergyStuffResponseDtoList = allergyStuffService.getAllergyStuffListByCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(allergyStuffResponseDtoList);
    }

    /**
     * 알러지 원재료 삭제 API
     * @param stuffId
     * @return String
     */
    @DeleteMapping("/allergyStuff/{stuffId}")
    public ResponseEntity<String> deleteAllergyStuff(@PathVariable Long stuffId) {
        allergyStuffService.deleteAllergyStuff(stuffId);

        return ResponseEntity.status(HttpStatus.OK).body("알러지 원재료 삭제가 완료되었습니다.");
    }
}
