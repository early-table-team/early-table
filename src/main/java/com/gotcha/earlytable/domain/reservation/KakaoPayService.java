package com.gotcha.earlytable.domain.reservation;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateResponseDto;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.gotcha.earlytable.domain.reservation.dto.KakaoPayRequestDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class KakaoPayService {


    private final String cid = "TC0ONETIME";
    private final String KAKAO_PAY_API_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private final String KAKAO_API_KEY = "DEV9E312D5439CC59166E19F60620069872523B0";
    private final MenuRepository menuRepository;
    private final ReservationRepository reservationRepository;

    public KakaoPayService(MenuRepository menuRepository, ReservationRepository reservationRepository) {
        this.menuRepository = menuRepository;
        this.reservationRepository = reservationRepository;
    }



    public String preparePayment(ReservationCreateResponseDto reservation) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + KAKAO_API_KEY);

        Map<Long, Long> menus = reservation.extractMenuIdAndQuantities();
        long total = 0L;

        for (Map.Entry<Long, Long> entry : menus.entrySet()) {
            Menu menu = menuRepository.findByIdOrElseThrow(entry.getKey());
            total += menu.getMenuPrice() * entry.getValue();
        }

        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", String.valueOf(reservation.getReservationId()));
        params.put("partner_user_id", String.valueOf(reservation.getUserId()));
        params.put("item_name", "Reservation Payment");
        params.put("quantity", "1");
        params.put("total_amount", String.valueOf(total));
        params.put("vat_amount", "200");
        params.put("tax_free_amount", "0");
        params.put("approval_url", "http://localhost:3000/processing");
        params.put("fail_url", "http://localhost:3000/payment-fail");
        params.put("cancel_url", "http://localhost:3000/payment-cancel");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            // 카카오페이 결제 준비 요청
            System.out.println("Sending request with params: " + params);
            System.out.println("Sending request with headers: " + headers);
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("User ID: " + reservation.getUserId());

            ResponseEntity<Map> response = restTemplate.exchange(KAKAO_PAY_API_URL, HttpMethod.POST, requestEntity, Map.class);

            // 응답 받기
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Response Status Code: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());
                // 성공적인 응답 처리
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("next_redirect_pc_url")) {
                    String tid = (String) responseBody.get("tid");
                    System.out.println(tid);
                    String partner_order_id = (String) responseBody.get("partner_order_id");
                    String partner_user_id = (String) responseBody.get("partner_user_id");
                    System.out.println(partner_order_id);
                    System.out.println(partner_user_id);

                    reservation.setTid(tid);
                    // 결제 준비 URL 반환
                    return (String) responseBody.get("next_redirect_pc_url");
                } else {
                    throw new RuntimeException("카카오페이 응답에 next_redirect_pc_url 없음");
                }
            } else {

                throw new RuntimeException("카카오페이 결제 준비 요청 실패, 상태 코드: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("카카오페이 결제 준비 중 오류 발생", e);
            throw new RuntimeException("카카오페이 결제 준비 중 오류 발생", e);
        }


    }


    public String approvePayment(String tid, String partnerOrderId, String partnerUserId, String pgToken) {
        log.info("[approvePayment] 결제 승인 요청 시작");
        log.info("tid: {}, partnerOrderId: {}, partnerUserId: {}, pgToken: {}", tid, partnerOrderId, partnerUserId, pgToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + KAKAO_API_KEY);

        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", partnerOrderId);
        params.put("partner_user_id", partnerUserId);
        params.put("pg_token", pgToken);

        log.info("[approvePayment] 요청 파라미터: {}", params);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            log.info("[approvePayment] 카카오페이 API 요청 전송");

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            log.info("[approvePayment] 카카오페이 응답 코드: {}", response.getStatusCode());
            log.info("[approvePayment] 카카오페이 응답 본문: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("aid")) {
                    Long reservationId = Long.parseLong(partnerOrderId);
                    Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
                    reservation.modifyStatus(ReservationStatus.CASHED);
                    reservationRepository.save(reservation);

                    log.info("[approvePayment] 결제 승인 완료: aid = {}", responseBody.get("aid"));
                    return "결제 승인 완료, aid: " + responseBody.get("aid");
                } else {
                    throw new RuntimeException("[approvePayment] 카카오페이 응답에 결제 승인 정보 없음");
                }
            } else {
                throw new RuntimeException("[approvePayment] 카카오페이 결제 승인 요청 실패, 상태 코드: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("[approvePayment] 카카오페이 결제 승인 중 오류 발생", e);
            throw new RuntimeException("[approvePayment] 카카오페이 결제 승인 중 오류 발생: " + e.getMessage());
        }
    }



    public String cancelPayment(String tid, String partnerOrderId, String partnerUserId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + KAKAO_API_KEY);

        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid); // 결제 고유번호
        params.put("partner_order_id", partnerOrderId); // 가맹점 주문번호
        params.put("partner_user_id", partnerUserId); // 가맹점 회원 ID

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            // 카카오페이 결제 취소 요청
            ResponseEntity<Map> response = restTemplate.exchange("https://open-api.kakaopay.com/online/v1/payment/cancel", HttpMethod.POST, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // 응답 처리
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("aid")) {
                    Long reservationId = Long.parseLong(partnerOrderId);  // reservationId를 partnerOrderId로 사용
                    Reservation reservation = reservationRepository.findByIdOrElseThrow(reservationId);
                    reservation.modifyStatus(ReservationStatus.CANCELED);
                    reservationRepository.save(reservation);
                    // 결제 취소 성공
                    return "결제 취소 완료, aid: " + responseBody.get("aid");
                } else {
                    throw new RuntimeException("카카오페이 응답에 결제 취소 정보 없음");
                }
            } else {
                throw new RuntimeException("카카오페이 결제 취소 요청 실패, 상태 코드: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("카카오페이 결제 취소 중 오류 발생", e);
            throw new RuntimeException("카카오페이 결제 취소 중 오류 발생", e);
        }
    }

}
