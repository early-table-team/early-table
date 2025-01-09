package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.store.entity.ReservationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationMasterRepository extends JpaRepository<ReservationMaster, Long> {

}
