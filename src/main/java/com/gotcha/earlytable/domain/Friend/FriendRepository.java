package com.gotcha.earlytable.domain.Friend;

import com.gotcha.earlytable.domain.Friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
}
