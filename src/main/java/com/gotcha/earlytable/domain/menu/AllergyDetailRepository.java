package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.entity.AllergyDetail;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyDetailRepository extends JpaRepository<AllergyDetail, Long> {

    default AllergyDetail findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }
}
