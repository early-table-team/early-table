package com.gotcha.earlytable.domain.reservation;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateResponseDto;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.global.enums.PartyRole;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", cid); // 가맹점 코드
        requestBody.put("partner_order_id", String.valueOf(reservation.getReservationId())); // 가맹점 주문번호
        requestBody.put("partner_user_id", String.valueOf(reservation.getUserId())); // 가맹점 회원 ID
        requestBody.put("item_name", "Reservation Payment"); // 상품명
        requestBody.put("quantity", 1); // 상품 수량
        requestBody.put("total_amount", total); // 결제 금액  -> 어차피 뱃은 별도로 들어감 금액 / 11
        requestBody.put("tax_free_amount", 100); // 비과세 금액
        requestBody.put("vat_amount",100);
        requestBody.put("approval_url", "https://www.earlytable.kr/processing"); // 결제 승인 URL
        requestBody.put("fail_url", "https://www.earlytable.kr/payment-fail"); // 결제 실패 URL
        requestBody.put("cancel_url", "https://www.earlytable.kr/home"); // 결제 취소 URL

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 카카오페이 결제 준비 요청
            System.out.println("Sending request with params: " + requestBody);
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
                    Reservation res = reservationRepository.findByIdOrElseThrow(reservation.getReservationId());
                    res.setTid(tid);
                    res.setAmount(total);
                    reservationRepository.save(res);

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
                    log.info("[approvePayment] 결제 승인 완료: aid = {}", responseBody.get("aid"));
                    Map<String, Object> amount = (Map<String, Object>) responseBody.get("amount");

                    if (amount != null) {
                        Integer totalAmount = (Integer) amount.get("total"); // 전체 결제 금액
                        log.info("[approvePayment] 결제 금액: {}", totalAmount);
                    }
                    Reservation res = reservationRepository.findByIdOrElseThrow(Long.parseLong(partnerOrderId));
                    res.modifyStatus(ReservationStatus.PENDING);
                    reservationRepository.save(res);


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



    public String cancelPayment(String partnerOrderId) {
        Reservation reservation = reservationRepository.findByIdOrElseThrow(Long.parseLong(partnerOrderId));

        String tid = reservation.getTid();
        String partnerUserId = reservation.getParty().getPartyPeople().stream()
                .filter(partyPeople -> partyPeople.getPartyRole().equals(PartyRole.REPRESENTATIVE)) // 대표자 필터링
                .map(partyPeople -> String.valueOf(partyPeople.getUser().getId())) // userId를 String으로 변환
                .findFirst() // 첫 번째 값 가져오기
                .orElseThrow(() -> new RuntimeException("대표자를 찾을 수 없습니다."));
        Integer cancelAmount =reservation.getAmount().intValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "SECRET_KEY " + KAKAO_API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", cid); // 가맹점 코드
        requestBody.put("tid", tid); // 결제 고유번호
        requestBody.put("cancel_amount", cancelAmount); // 취소 금액
        requestBody.put("cancel_tax_free_amount", 100); // 취소 비과세 금액
        requestBody.put("cancel_vat_amount",100);
        log.info(String.valueOf(cancelAmount));


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 카카오페이 결제 취소 요청
            ResponseEntity<Map> response = restTemplate.exchange("https://open-api.kakaopay.com/online/v1/payment/cancel", HttpMethod.POST, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // 응답 처리
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("aid")) {
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
