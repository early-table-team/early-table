package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.MenuRequestDto;
import com.gotcha.earlytable.domain.menu.dto.MenuResponseDto;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/menus")
public class MenuController {

    private final MenuService menuService;
    private final MenuRepository menuRepository;

    public MenuController(MenuService menuService, MenuRepository menuRepository) {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
    }

    /**
     * 메뉴 생성 API
     * @param storeId
     * @param menuRequestDto
     * @return MenuResponseDto
     */
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(@PathVariable Long storeId,
                                                      @ModelAttribute MenuRequestDto menuRequestDto) throws IOException {

        MenuResponseDto createMenuResponseDto = menuService.createMenu(storeId, menuRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createMenuResponseDto);
    }

    /**
     * 메뉴 수정 API
     * @param storeId
     * @param menuId
     * @param menuRequestDto
     * @return MenuResponseDto
     */
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long storeId, @PathVariable Long menuId,
                                                      @ModelAttribute MenuRequestDto menuRequestDto) {
        MenuResponseDto updateMenuResponseDto = menuService.updateMenu(storeId, menuId, menuRequestDto);

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
    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long menuId) {

        menuService.deleteMenu(menuId);

        return ResponseEntity.status(HttpStatus.OK).body("메뉴 삭제가 완료되었습니다.");
    }

}
