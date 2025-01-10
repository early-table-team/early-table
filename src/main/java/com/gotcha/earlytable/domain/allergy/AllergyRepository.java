package com.gotcha.earlytable.domain.allergy;

import com.gotcha.earlytable.domain.allergy.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

}
