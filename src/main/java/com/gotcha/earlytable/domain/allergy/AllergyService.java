package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.dto.AllergyRequestDto;
import com.gotcha.earlytable.domain.allergy.dto.AllergyResponseDto;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.allergy.entity.Allergy;
import com.gotcha.earlytable.domain.allergy.entity.AllergyStuff;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.ConflictException;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import com.gotcha.earlytable.global.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllergyService {
    private final AllergyRepository allergyRepository;
    private final AllergyStuffRepository allergyStuffRepository;
    private final MenuRepository menuRepository;

    public AllergyService(AllergyRepository allergyRepository, AllergyStuffRepository allergyStuffRepository, MenuRepository menuRepository) {
        this.allergyRepository = allergyRepository;
        this.allergyStuffRepository = allergyStuffRepository;
        this.menuRepository = menuRepository;
    }

    /**
     * 메뉴에 알러지 등록 서비스 메서드
     */
    @Transactional
    public void createAllergyInMenu(Long menuId, AllergyRequestDto allergyRequestDto, Long userId) {

        //메뉴 정보 가져오기
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        //가게 주인인지 확인
        if(!menu.getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }

        //없는 원재료 입력 시, 예외처리
        if(!allergyStuffRepository.existsByAllergyStuff(allergyRequestDto.getAllergyStuff()))
            throw new NotFoundException(ErrorCode.NOT_FOUND_ALLERGY_STUFF);

        //사용자가 알러지 원재료명(Dto)을 입력 -> AllergyStuff 반환
        AllergyStuff allergyStuff = allergyStuffRepository.findByAllergyStuff(allergyRequestDto.getAllergyStuff());

        //이미 등록한 원재료 재입력 시, 예외처리
        if(allergyRepository.existsByMenuMenuIdAndAllergyStuffAllergyStuffId(menuId, allergyStuff.getAllergyStuffId())) {
            throw new ConflictException(ErrorCode.ALREADY_REGISTERED_ALLERGY_STUFF_IN_MENU);
        }

        //알러지 생성
        Allergy allergy = new Allergy(menu,allergyStuff);

        Allergy savedAllergy = allergyRepository.save(allergy);
    }

    /**
     * 메뉴에 등록된 알러지 조회 서비스 메서드
     */
    public List<AllergyResponseDto> getAllergyListInMenu(Long menuId) {
        //메뉴아이디 -> 메뉴에 해당하는 알러지 리스트(알러지ID, 원재료ID)
        List<Allergy> allergyList = allergyRepository.findAllByMenuMenuId(menuId);

        List<AllergyResponseDto> allergyResponseDtoList = new ArrayList<>();
        String allergyCategoryName = "";
        String allergyStuffName = "";
        for (Allergy allergy: allergyList) {
            //알러지 원재료 가져오기
            AllergyStuff allergyStuff = allergyStuffRepository.findByIdOrElseThrow(allergy.getAllergyStuff().getAllergyStuffId());
            allergyCategoryName = allergyStuff.getAllergyCategory().getAllergyCategory();
            allergyStuffName = allergyStuff.getAllergyStuff();

            AllergyResponseDto allergyResponseDto = new AllergyResponseDto(allergy.getAllergyId(), allergyCategoryName, allergyStuffName);

            allergyResponseDtoList.add(allergyResponseDto);
        }

       return allergyResponseDtoList;
    }

    /**
     * 메뉴에 등록한 알러지 삭제 서비스 메서드
     */
    @Transactional
    public void deleteAllergyInMenu(Long allergyId, Long userId) {
        if(!allergyRepository.existsById(allergyId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        //가게 주인인지 확인
        Allergy allergy = allergyRepository.findByIdOrElseThrow(allergyId);
        if(!allergy.getMenu().getStore().getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.NO_STORE_OWNER);
        }

        allergyRepository.deleteById(allergyId);
    }

}
