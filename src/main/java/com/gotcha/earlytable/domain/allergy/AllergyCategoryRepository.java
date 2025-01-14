package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.entity.AllergyCategory;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyCategoryRepository extends JpaRepository<AllergyCategory, Long> {

    default AllergyCategory findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsByAllergyCategory(String allergyCategory);
}
