package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.ImageFile;
import com.gotcha.earlytable.domain.menu.dto.MenuRequestDto;
import com.gotcha.earlytable.domain.menu.dto.MenuResponseDto;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final FileService fileService;

    public MenuService(MenuRepository menuRepository, StoreRepository storeRepository, FileService fileService) {
        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
        this.fileService = fileService;
    }

    /**
     * 메뉴 생성 서비스 메서드
     */
    @Transactional
    public MenuResponseDto createMenu(Long storeId, MenuRequestDto createMenuRequestDto) throws IOException {

//        //이미지가 있을 경우
//        String imageUrl = null;
//        if (image != null && !image.isEmpty()) {
//            String imageFileResponseDto = fileService.createImageFile(image);
//
//            //url 가져오기
//            if(!imageFileResponseDto.isEmpty()) {
//                imageUrl = imageFileResponseDto;
//            }
//        }

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        File file = fileService.createFile();

        //메뉴 생성
        Menu menu = new Menu(
                createMenuRequestDto.getMenuName(),
                createMenuRequestDto.getMenuContents(),
                createMenuRequestDto.getMenuPrice(),
                createMenuRequestDto.getMenuStatus(),
                file,
                store
        );

        //메뉴 저장
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDto.toDto(savedMenu);
    }

    /**
     * 메뉴 수정 서비스 메서드
     */
    @Transactional
    public MenuResponseDto updateMenu(Long storeId, Long menuId, MenuRequestDto menuRequestDto) {
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        //메뉴 수정
        menu.update(menuRequestDto);

        //메뉴 저장
        menuRepository.save(menu);

        return MenuResponseDto.toDto(menu);
    }

    /**
     * 메뉴 전체 조회 서비스 메서드
     */
    public List<MenuResponseDto> getMenus(Long storeId) {
        List<Menu> menus = menuRepository.findAllByStoreStoreId(storeId);

        return menus.stream().map(MenuResponseDto::toDto).toList();
    }

    /**
     * 메뉴 삭제 서비스 메서드
     */
    public void deleteMenu(Long menuId) {
        menuRepository.deleteById(menuId);
    }
}