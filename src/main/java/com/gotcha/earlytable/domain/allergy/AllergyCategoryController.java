package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.dto.AllergyCategoryRequestDto;
import com.gotcha.earlytable.domain.allergy.dto.AllergyCategoryResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allergies")
public class AllergyCategoryController {
    private final AllergyCategoryService allergyCategoryService;

    public AllergyCategoryController(AllergyCategoryService allergyCategoryService) {
        this.allergyCategoryService = allergyCategoryService;
    }

    /**
     * 알러지 카테고리 생성 API
     * @param allergyCategoryRequestDto
     * @return AllergyCategoryResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PostMapping
    public ResponseEntity<AllergyCategoryResponseDto> createAllergyCategory(@Valid @RequestBody AllergyCategoryRequestDto allergyCategoryRequestDto) {
        AllergyCategoryResponseDto createAllergyCategoryResponseDto = allergyCategoryService.createAllergyCategory(allergyCategoryRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createAllergyCategoryResponseDto);
    }

    /**
     * 알러지 카테고리 수정 API
     * @param categoryId
     * @param allergyCategoryRequestDto
     * @return AllergyCategoryResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @PatchMapping("/{categoryId}")
    public ResponseEntity<AllergyCategoryResponseDto> updateAllergyCategory(@PathVariable Long categoryId,
                                                                            @Valid @RequestBody AllergyCategoryRequestDto allergyCategoryRequestDto) {
        AllergyCategoryResponseDto updateAllergyCategoryResponseDto = allergyCategoryService.updateAllergyCategory(categoryId, allergyCategoryRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateAllergyCategoryResponseDto);
    }

    /**
     * 알러지 카테고리 전체 조회 API
     * @return List<AllergyCategoryResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<AllergyCategoryResponseDto>> getAllergyCategoryList() {
        List<AllergyCategoryResponseDto> allergyCategoryResponseDtoList = allergyCategoryService.getAllergyCategoryList();

        return ResponseEntity.status(HttpStatus.OK).body(allergyCategoryResponseDtoList);
    }

    /**
     * 알러지 카테고리 삭제 API
     * @param categoryId
     * @return String
     */
    @CheckUserAuth(requiredAuthorities = {Auth.ADMIN})
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteAllergyCategory(@PathVariable Long categoryId) {
        allergyCategoryService.deleteAllergyCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body("알러지 카테고리 삭제가 완료되었습니다.");
    }
}
