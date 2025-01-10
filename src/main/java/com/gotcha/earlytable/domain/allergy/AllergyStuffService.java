package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.dto.AllergyStuffRequestDto;
import com.gotcha.earlytable.domain.allergy.dto.AllergyStuffResponseDto;
import com.gotcha.earlytable.domain.allergy.entity.AllergyCategory;
import com.gotcha.earlytable.domain.allergy.entity.AllergyStuff;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AllergyStuffService {
    private final AllergyStuffRepository allergyStuffRepository;
    private final AllergyCategoryRepository allergyCategoryRepository;

    public AllergyStuffService(AllergyStuffRepository allergyStuffRepository, AllergyCategoryRepository allergyCategoryRepository) {
        this.allergyStuffRepository = allergyStuffRepository;
        this.allergyCategoryRepository = allergyCategoryRepository;
    }

    /**
     * 알러지 원재료 등록 서비스 메서드
     * @param categoryId
     * @param allergyStuffRequestDto
     * @return
     */
    @Transactional
    public AllergyStuffResponseDto createAllergyStuff(Long categoryId, AllergyStuffRequestDto allergyStuffRequestDto) {
        //알러지 카테고리 받아오기
        AllergyCategory allergyCategory = allergyCategoryRepository.findByIdOrElseThrow(categoryId);

        AllergyStuff allergyStuff = new AllergyStuff(
                allergyStuffRequestDto.getAllergyStuff(),
                allergyCategory
        );

        AllergyStuff savedAllergyStuff = allergyStuffRepository.save(allergyStuff);

        return AllergyStuffResponseDto.toDto(savedAllergyStuff);
    }

    /**
     * 알러지 원재료 수정 서비스 메서드
     */
    @Transactional
    public AllergyStuffResponseDto updateAllergyStuff(Long categoryId, Long stuffId, AllergyStuffRequestDto allergyStuffRequestDto) {
        AllergyStuff allergyStuff = allergyStuffRepository.findByIdOrElseThrow(stuffId);

        allergyStuff.update(allergyStuffRequestDto);

        AllergyStuff updatedAllergyStuff = allergyStuffRepository.save(allergyStuff);

        return AllergyStuffResponseDto.toDto(updatedAllergyStuff);
    }

    /**
     * 카테고리 기준 알러지 원재료 조회 서비스 메서드
     */
    public List<AllergyStuffResponseDto> getAllergyStuffListByCategory(Long categoryId) {
        AllergyCategory allergyCategory = allergyCategoryRepository.findByIdOrElseThrow(categoryId);
        List<AllergyStuff> allergyStuffs = allergyStuffRepository.findAllByAllergyCategory(allergyCategory);

        return allergyStuffs.stream().map(AllergyStuffResponseDto::toDto).toList();
    }

    /**
     * 알러지 원재료 삭제 서비스 메서드
     */
    @Transactional
    public void deleteAllergyStuff(Long stuffId) {
        if(!allergyStuffRepository.existsById(stuffId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        allergyStuffRepository.deleteById(stuffId);
    }
}
