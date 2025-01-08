package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.AllergyDetailRequestDto;
import com.gotcha.earlytable.domain.menu.dto.AllergyDetailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allergies")
public class AllergyDetailController {

    private final AllergyDetailService allergyDetailService;

    public AllergyDetailController(AllergyDetailService allergyDetailService) {
        this.allergyDetailService = allergyDetailService;
    }

    /**
     * 알러지 디테일 생성 API
     * @param allergyDetailRequestDto
     * @return AllergyDetailResponseDto
     */
    @PostMapping
    public ResponseEntity<AllergyDetailResponseDto> createAllergyDetail(@ModelAttribute AllergyDetailRequestDto allergyDetailRequestDto) {
        AllergyDetailResponseDto createAllergyDetailResponseDto = allergyDetailService.createAllergyDetail(allergyDetailRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createAllergyDetailResponseDto);
    }

    /**
     * 알러지 디테일 수정 API
     * @param allergyId
     * @param allergyDetailRequestDto
     * @return AllergyDetailResponseDto
     */
    @PatchMapping("/{allergyId}")
    public ResponseEntity<AllergyDetailResponseDto> updateAllergyDetail(@PathVariable Long allergyId,
                                                                        @ModelAttribute AllergyDetailRequestDto allergyDetailRequestDto) {
        AllergyDetailResponseDto updateAllergyDetailResponseDto = allergyDetailService.updateAllergyDetail(allergyId, allergyDetailRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateAllergyDetailResponseDto);
    }

    /**
     * 알러지 전체 조회 API
     * @return List<AllergyDetailResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<AllergyDetailResponseDto>> getAllergyList() {
        List<AllergyDetailResponseDto> allergyDetailResponseDtoList = allergyDetailService.getAllergyList();

        return ResponseEntity.status(HttpStatus.OK).body(allergyDetailResponseDtoList);
    }

    /**
     * 알러지 삭제 API
     * @param allergyId
     * @return String
     */
    @DeleteMapping("/{allergyId}")
    public ResponseEntity<String> deleteAllergyDetail(@PathVariable Long allergyId) {
        allergyDetailService.deleteAllergyDetail(allergyId);

        return ResponseEntity.status(HttpStatus.OK).body("알러지 디테일 삭제가 완료되었습니다.");
    }
}
