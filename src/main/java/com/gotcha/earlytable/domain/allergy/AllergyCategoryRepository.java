package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.entity.AllergyCategory;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyCategoryRepository extends JpaRepository<AllergyCategory, Long> {

    default AllergyCategory findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsByAllergyCategory(String allergyCategory);

    @Query("SELECT ac.allergyCategoryId, ac.allergyCategory, asf.allergyStuffId, asf.allergyStuff " +
            "FROM AllergyCategory ac " +
            "LEFT JOIN AllergyStuff asf ON ac.allergyCategoryId = asf.allergyCategory.allergyCategoryId")
    List<Object[]> findAllCategoryWithStuff();
}
