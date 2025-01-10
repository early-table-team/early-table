package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.dto.AllergyCategoryRequestDto;
import com.gotcha.earlytable.domain.allergy.dto.AllergyCategoryResponseDto;
import com.gotcha.earlytable.domain.allergy.entity.AllergyCategory;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AllergyCategoryService {
    private final AllergyCategoryRepository allergyCategoryRepository;

    public AllergyCategoryService(AllergyCategoryRepository allergyCategoryRepository) {
        this.allergyCategoryRepository = allergyCategoryRepository;
    }

    /**
     * 알러지 카테고리 등록 서비스 메서드
     */
    @Transactional
    public AllergyCategoryResponseDto createAllergyCategory(AllergyCategoryRequestDto allergyCategoryRequestDto) {
        AllergyCategory allergyCategory = new AllergyCategory(
                allergyCategoryRequestDto.getAllergyCategory()
        );

        AllergyCategory savedAllergyCategory = allergyCategoryRepository.save(allergyCategory);

        return AllergyCategoryResponseDto.toDto(savedAllergyCategory);
    }

    /**
     * 알러지 카테고리 수정 서비스 메서드
     */
    @Transactional
    public AllergyCategoryResponseDto updateAllergyCategory(Long categoryId, AllergyCategoryRequestDto allergyCategoryRequestDto) {
        AllergyCategory allergyCategory = allergyCategoryRepository.findByIdOrElseThrow(categoryId);

        allergyCategory.update(allergyCategoryRequestDto);

        AllergyCategory updatedAllergyCategory = allergyCategoryRepository.save(allergyCategory);

        return AllergyCategoryResponseDto.toDto(updatedAllergyCategory);
    }

    /**
     * 알러지 카테고리 전체 조회 서비스 메서드
     */
    public List<AllergyCategoryResponseDto> getAllergyCategoryList() {
        List<AllergyCategory> allergyCategoryList = allergyCategoryRepository.findAll();

        return allergyCategoryList.stream().map(AllergyCategoryResponseDto::toDto).toList();
    }

    /**
     * 알러지 카테고리 삭제 서비스 메서드
     */
    @Transactional
    public void deleteAllergyCategory(Long categoryId) {
        if(!allergyCategoryRepository.existsById(categoryId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        allergyCategoryRepository.deleteById(categoryId);
    }
}
