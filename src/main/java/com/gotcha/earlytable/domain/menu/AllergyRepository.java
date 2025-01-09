package com.gotcha.earlytable.domain.menu;

import com.gotcha.earlytable.domain.menu.entity.Allergy;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

}
