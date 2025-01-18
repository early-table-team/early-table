package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.reservation.dto.*;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<ReservationCreateResponseDto> createReservation(@PathVariable Long storeId,
                                                                          @Valid @RequestBody ReservationCreateRequestDto requestDto,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReservationCreateResponseDto responseDto = reservationService.createReservation(storeId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     *  예약 전체조회 API
     * @param userDetails
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationGetAllResponseDto>> getAllReservations(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                 @RequestParam(value = "size", defaultValue = "5") int size ) {

        List<ReservationGetAllResponseDto> resDto = reservationService.getAllReservations(userDetails.getUser(),page, size);

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    /**
     *  예약 단건 조회 API
     * @param reservationId
     * @param userDetails
     * @return  ResponseEntity<ReservationGetOneResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationGetOneResponseDto> getReservation(@PathVariable Long reservationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReservationGetOneResponseDto resDto = reservationService.getReservation(reservationId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    /**
     *  예약 메뉴 변경 API
     * @param reservationId
     * @param userDetails
     * @return
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @PatchMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationGetOneResponseDto> updateReservation(@PathVariable Long reservationId,
                                                                          @RequestBody ReservationUpdateRequestDto requestDto,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ReservationGetOneResponseDto resDto = reservationService.updateReservation(reservationId, userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }


    /**
     *  예약 취소 API
     * @param reservationId
     * @return  ResponseEntity<String>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reservationService.cancelReservation(reservationId, userDetails.getUser());

        //취소는 NO_CONTENT 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     *   가게 오너 입장에서 예약 조회 API
     * @param reservationDate
     * @param storeId
     * @return  ResponseEntity<List<OwnerReservationResponseDto>>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping("/owner/reservations")
    public ResponseEntity<List<OwnerReservationResponseDto>> getStoreReservations(@RequestParam("reservationDate") LocalDate reservationDate,
                                                                                  @RequestParam("storeId") Long storeId){

        List<OwnerReservationResponseDto> responseDto =reservationService.getStoreReservations(reservationDate, storeId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }



}
