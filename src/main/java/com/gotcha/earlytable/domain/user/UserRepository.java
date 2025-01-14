package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.Status;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);

    boolean existsUserByPhone(String phone);

    Optional<User> findByEmailAndStatus(String email, Status status);

    default User findByEmailOrElseThrow(String email) {
        return findByEmailAndStatus(email, Status.NORMAL).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    Optional<User> findByPhone(String phoneNumber);
}
