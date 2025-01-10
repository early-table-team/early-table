package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.entity.AllergyCategory;
import com.gotcha.earlytable.domain.allergy.entity.AllergyStuff;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyStuffRepository extends JpaRepository<AllergyStuff, Long> {

    default AllergyStuff findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    List<AllergyStuff> findAllByAllergyCategory(AllergyCategory allergyCategory);

    AllergyStuff findByAllergyStuff(String allergyStuff);
}
