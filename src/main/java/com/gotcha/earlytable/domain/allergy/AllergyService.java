package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.dto.AllergyRequestDto;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.allergy.entity.Allergy;
import com.gotcha.earlytable.domain.allergy.entity.AllergyStuff;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void createAllergyInMenu(Long menuId, AllergyRequestDto allergyRequestDto) {

        //사용자가 알러지 원재료명(Dto)을 입력 -> AllergyStuff 반환
        AllergyStuff allergyStuff = allergyStuffRepository.findByAllergyStuff(allergyRequestDto.getAllergyStuff());

        //메뉴 정보 가져오기
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);

        //알러지 생성
        Allergy allergy = new Allergy(menu,allergyStuff);

        Allergy savedAllergy = allergyRepository.save(allergy);
    }

    /**
     * 메뉴에 등록한 알러지 삭제 서비스 메서드
     */
    @Transactional
    public void deleteAllergyInMenu(Long allergyId) {
        if(allergyRepository.existsById(allergyId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        allergyRepository.deleteById(allergyId);
    }
}
