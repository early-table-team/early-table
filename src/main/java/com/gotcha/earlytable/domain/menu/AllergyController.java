package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.AllergyRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
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
    @PostMapping("/stores/{storeId}/menus/{menuId}/allergy")
    public ResponseEntity<String> createAllergyInMenu(@PathVariable Long menuId,
                                                      @ModelAttribute AllergyRequestDto allergyRequestDto) {

        allergyService.createAllergyInMenu(menuId, allergyRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("해당 메뉴에 알러지정보가 등록되었습니다.");
    }
}
