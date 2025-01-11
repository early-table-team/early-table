package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.file.FileDetailService;
import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.file.entity.File;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.menu.dto.MenuRequestDto;
import com.gotcha.earlytable.domain.menu.dto.MenuResponseDto;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.store.StoreRepository;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final FileService fileService;
    private final FileDetailService fileDetailService;

    public MenuService(MenuRepository menuRepository, StoreRepository storeRepository,
                       FileService fileService, FileDetailService fileDetailService) {

        this.menuRepository = menuRepository;
        this.storeRepository = storeRepository;
        this.fileService = fileService;
        this.fileDetailService = fileDetailService;
    }

    /**
     * 메뉴 생성 서비스 메서드
     */
    @Transactional
    public MenuResponseDto createMenu(Long storeId, Long userId, MenuRequestDto requestDto) {

        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 가게 주인인지 확인
        if(!store.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }

        // 파일 생성
        File file = fileService.createFile();

        // 프로필 이미지 파일 저장
        String imageUrl = fileDetailService.createImageFile(requestDto.getImage(), file);

        //메뉴 생성
        Menu menu = new Menu(requestDto.getMenuName(),
                requestDto.getMenuContents(),
                requestDto.getMenuPrice(),
                requestDto.getMenuStatus(),
                file, store
        );

        //메뉴 저장
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDto.toDto(savedMenu, imageUrl);
    }

    /**
     * 메뉴 수정 서비스 메서드
     */
    @Transactional
    public MenuResponseDto updateMenu(Long menuId, Long userId, MenuRequestDto menuRequestDto) {

        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        // 가게 주인인지 확인
        if(!menu.getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }

        //메뉴 수정 및 저장
        menu.updateMenu(menuRequestDto);
        Menu savedMenu = menuRepository.save(menu);

        String imageUrl = menu.getFile().getFileDetailList().stream()
                .findAny().map(FileDetail::getFileUrl).orElse(null);

        if(menuRequestDto.getImage() != null) {

            // 기존 이미지 제거
            menu.getFile().getFileDetailList().stream()
                    .findAny().ifPresent(fileDetail -> fileDetailService.deleteImageFile(fileDetail.getFileUrl()));

            // 새로 생성
            imageUrl = fileDetailService.createImageFile(menuRequestDto.getImage(), menu.getFile());
        }

        return MenuResponseDto.toDto(savedMenu, imageUrl);
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
    @Transactional
    public void deleteMenu(Long menuId) {
        if(!menuRepository.existsById(menuId)){
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        
        menuRepository.deleteById(menuId);
    }
}
