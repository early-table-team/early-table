package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateRequestDto;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateResponseDto;
import com.gotcha.earlytable.domain.reservation.dto.ReservationGetAllResponseDto;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * 예약 잡기 API
     * @param storeId
     * @param requestDto
     * @param userDetails
     * @return  ResponseEntity<ReservationCreateResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PostMapping("/stores/{storeId}/reservations")
    public ResponseEntity<ReservationCreateResponseDto> createReservation(@PathVariable Long storeId, @RequestBody ReservationCreateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReservationCreateResponseDto resDto = reservationService.createReservation(storeId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    /**
     *  예약 전체조회 API
     * @param userDetails
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("reservations")
    public ResponseEntity<List<ReservationGetAllResponseDto>> getAllReservations(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ReservationGetAllResponseDto> resDto = reservationService.getAllReservations(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }


}
