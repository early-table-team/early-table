package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.reservation.dto.*;
import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.config.auth.UserDetailsImpl;
import com.gotcha.earlytable.global.enums.Auth;
import com.gotcha.earlytable.global.error.ErrorCode;
import com.gotcha.earlytable.global.error.exception.CustomException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final KakaoPayService kakaoPayService;

    public ReservationController(ReservationService reservationService, KakaoPayService kakaoPayService) {
        this.reservationService = reservationService;
        this.kakaoPayService = kakaoPayService;
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

        // 예약 생성
        ReservationCreateResponseDto responseDto = reservationService.createReservation(storeId, requestDto, userDetails.getUser());

        try {
            // 카카오페이 결제 URL 생성
            String paymentRedirectUrl = kakaoPayService.preparePayment(responseDto);
            responseDto.setPaymentUrl(paymentRedirectUrl);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NOT_FOUND_DAY);
        }
        System.out.println(responseDto.getUserId());

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
                                                                                 @PageableDefault Pageable pageable) {

        List<ReservationGetAllResponseDto> resDto = reservationService.getAllReservations(userDetails.getUser(), pageable);

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

    /**
     * 결제 요청 승인
     * @param tid
     * @param partnerOrderId
     * @param partnerUserId
     * @param pgToken
     * @return
     */
    @PostMapping("/approve")
    public ResponseEntity<String> approvePayment(@RequestParam String tid,
                                                 @RequestParam String partnerOrderId,
                                                 @RequestParam String partnerUserId,
                                                 @RequestParam String pgToken){
        try {
            String result = kakaoPayService.approvePayment(tid, partnerOrderId, partnerUserId, pgToken);
            return ResponseEntity.ok(result);  // 결제 승인 완료 메시지 반환
        } catch (Exception e) {
            log.error("결제 승인 실패", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 승인 실패: " + e.getMessage());
        }
    }

    /**
     * 가게별 입장 유저 제한 api
     * @param storeId
     * @return
     */
    @PostMapping("/{storeId}/request")
    public ResponseEntity<?> requestReservation(@PathVariable Long storeId){
        // 가게별로 메뉴판에 들어갈 수 있는 사람을 100명으로 제한
        boolean isAccepted = reservationService.tryReserve(storeId);
        if (!isAccepted) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("예약 가능 인원이 초과되었습니다.");
        }

        return ResponseEntity.ok("예약이 접수되었습니다.");
    }

    /**
     *  가게 오너 입장에서 예약 단건 조회 API
     * @param reservationId
     * @param userDetails
     * @return  ResponseEntity<OwnerReservationResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @GetMapping("/owner/reservations/{reservationId}")
    public ResponseEntity<OwnerReservationResponseDto> getReservationDetail(@PathVariable Long reservationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        OwnerReservationResponseDto resDto = reservationService.getReservationDetail(reservationId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    /**
     * 가게 오너가 예약 상태 변경 API
     * @param reservationId
     * @param userDetails
     * @return ResponseEntity<OwnerReservationResponseDto>
     */
    @CheckUserAuth(requiredAuthorities = {Auth.OWNER})
    @PatchMapping("/owner/reservations/{reservationId}")
    public ResponseEntity<String> updateReservationStatus(@PathVariable Long reservationId,
                                                          @RequestBody ReservationUpdateStatusRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reservationService.updateReservationStatus(reservationId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body("예약 상태를 변경하였습니다.");
    }

}
