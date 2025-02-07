package com.gotcha.earlytable.domain.user;

import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import com.gotcha.earlytable.global.enums.Status;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery {

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

    @Query(value = "SELECT COUNT(*) " +
            "FROM party_people a " +
            "JOIN reservation b ON a.party_id = b.party_id " +
            "WHERE b.reservation_status = :reservationStatus " +
            "AND a.user_id = :userId", nativeQuery = true)
    long countPendingReservationsByUserNative(@Param("reservationStatus") String reservationStatus,
                                              @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) " +
            "FROM party_people a " +
            "JOIN waiting b ON a.party_id = b.party_id " +
            "WHERE b.waiting_status = :waitingStatus " +
            "AND a.user_id = :userId", nativeQuery = true)
    long countPendingWaitingsByUserNative(@Param("waitingStatus") String waitingStatus,
                                              @Param("userId") Long userId);
}
