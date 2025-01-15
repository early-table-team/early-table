package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.MenuRequestDto;
import com.gotcha.earlytable.domain.menu.dto.MenuResponseDto;
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
@RequestMapping("/stores/{storeId}/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 메뉴 생성 API
     * @param storeId
     * @param menuRequestDto
     * @return MenuResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@PathVariable Long storeId,
                                                      @Valid @ModelAttribute MenuRequestDto menuRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MenuResponseDto createMenuResponseDto = menuService.createMenu(storeId, userDetails.getUser().getId(), menuRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createMenuResponseDto);
    }

    /**
     * 메뉴 수정 API
     * @param menuId
     * @param menuRequestDto
     * @return MenuResponseDto
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long menuId,
                                                      @Valid @ModelAttribute MenuRequestDto menuRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MenuResponseDto updateMenuResponseDto = menuService.updateMenu(menuId, userDetails.getUser().getId(), menuRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateMenuResponseDto);
    }

    /**
     * 메뉴 전체 조회 API
     * @param storeId
     * @return MenuResponseDto
     */
    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenus(@PathVariable Long storeId) {
        List<MenuResponseDto> menuResponseDtoList = menuService.getMenus(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(menuResponseDtoList);
    }

    /**
     * 메뉴 삭제 API
     * @param menuId
     * @return String
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long storeId,@PathVariable Long menuId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        menuService.deleteMenu(storeId, menuId, userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body("메뉴 삭제가 완료되었습니다.");
    }

}
