package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.waiting.entity.WaitingNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingNumberRepository extends JpaRepository<WaitingNumber, Long> {

}
