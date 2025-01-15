package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.dto.AllergyRequestDto;
import com.gotcha.earlytable.domain.allergy.dto.AllergyResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping("/menus/{menuId}/allergies")
    public ResponseEntity<String> createAllergyInMenu(@PathVariable Long menuId,
                                                      @Valid @RequestBody AllergyRequestDto allergyRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        allergyService.createAllergyInMenu(menuId, allergyRequestDto, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body("해당 메뉴에 알러지정보가 등록되었습니다.");
    }

    /**
     * 메뉴에 등록된 알러지 조회 API
     * @param menuId
     * @return List<AllergyResponseDto>
     */
    @GetMapping("/menus/{menuId}/allergies")
    public ResponseEntity<List<AllergyResponseDto>> getAllergyListInMenu(@PathVariable Long menuId) {
        List<AllergyResponseDto> allergyResponseDtoList = allergyService.getAllergyListInMenu(menuId);

        return ResponseEntity.status(HttpStatus.OK).body(allergyResponseDtoList);
    }


    /**
     * 메뉴에 등록한 알러지 삭제 API
     * @param allergyId
     * @return String
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @DeleteMapping("/menus/allergies/{allergyId}")
    public ResponseEntity<String> deleteAllergyInMenu(@PathVariable Long allergyId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        allergyService.deleteAllergyInMenu(allergyId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body("등록한 알러지 정보 삭제가 완료되었습니다.");
    }
}
