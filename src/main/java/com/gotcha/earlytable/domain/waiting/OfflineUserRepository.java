package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.waiting.entity.OfflineUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfflineUserRepository extends JpaRepository<OfflineUser, Long> {

}

