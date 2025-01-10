package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.dto.AllergyRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AllergyController {
    private final AllergyService allergyService;

    public AllergyController(AllergyService allergyService, AllergyRepository allergyRepository) {
        this.allergyService = allergyService;
    }

    /**
     * 메뉴에 알러지 등록 API
     * @param menuId
     * @param allergyRequestDto //allergyStuff ex.아몬드
     * @return String
     */
    @PostMapping("/menus/{menuId}/allergies")
    public ResponseEntity<String> createAllergyInMenu(@PathVariable Long menuId,
                                                      @ModelAttribute AllergyRequestDto allergyRequestDto) {

        allergyService.createAllergyInMenu(menuId, allergyRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("해당 메뉴에 알러지정보가 등록되었습니다.");
    }

    /**
     * 메뉴에 등록한 알러지 삭제 API
     * @param allergyId
     * @return String
     */
    @DeleteMapping("/menus/allergies/{allergyId}")
    public ResponseEntity<String> deleteAllergyInMenu(@PathVariable Long allergyId) {
        allergyService.deleteAllergyInMenu(allergyId);

        return ResponseEntity.status(HttpStatus.OK).body("등록한 알러지 정보 삭제가 완료되었습니다.");
    }
}
