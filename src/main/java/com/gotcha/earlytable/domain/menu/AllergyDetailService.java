package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.dto.AllergyDetailRequestDto;
import com.gotcha.earlytable.domain.menu.dto.AllergyDetailResponseDto;
import com.gotcha.earlytable.domain.menu.entity.AllergyDetail;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AllergyDetailService {
    private final AllergyDetailRepository allergyDetailRepository;

    public AllergyDetailService(AllergyDetailRepository allergyDetailRepository) {
        this.allergyDetailRepository = allergyDetailRepository;
    }

    /**
     * 알러지 디테일 생성 서비스 메서드
     */
    @Transactional
    public AllergyDetailResponseDto createAllergyDetail(AllergyDetailRequestDto allergyDetailRequestDto) {
        AllergyDetail allergyDetail = new AllergyDetail(
                allergyDetailRequestDto.getAllergyName(),
                allergyDetailRequestDto.getAllergyStuff(),
                allergyDetailRequestDto.getAllergyContents()
        );

        return AllergyDetailResponseDto.toDto(allergyDetail);
    }

    /**
     * 알러지 디테일 수정 서비스 메서드
     */
    @Transactional
    public AllergyDetailResponseDto updateAllergyDetail(Long allergyId, AllergyDetailRequestDto allergyDetailRequestDto) {
        AllergyDetail allergyDetail = allergyDetailRepository.findByIdOrElseThrow(allergyId);

        //알러지 디테일 수정
        allergyDetail.updateAllergyDetail(allergyDetailRequestDto);

        //알러지 디테일 저장
        allergyDetailRepository.save(allergyDetail);

        return AllergyDetailResponseDto.toDto(allergyDetail);
    }

    /**
     * 알러지 디테일 전체 조회 서비스 메서드
     */
    public List<AllergyDetailResponseDto> getAllergyList() {
        List<AllergyDetail> allergyDetails = allergyDetailRepository.findAll();

        return allergyDetails.stream().map(AllergyDetailResponseDto::toDto).toList();
    }

    /**
     * 알러지 디테일 삭제 서비스 메서드
     */
    public void deleteAllergyDetail(Long allergyId) {
        if(!allergyDetailRepository.existsById(allergyId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        allergyDetailRepository.deleteById(allergyId);
    }
}
