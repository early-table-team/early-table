package com.gotcha.earlytable.domain.reservation;
import com.gotcha.earlytable.domain.menu.MenuRepository;
import com.gotcha.earlytable.domain.menu.entity.Menu;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateResponseDto;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.gotcha.earlytable.domain.reservation.dto.KakaoPayRequestDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KakaoPayService {


    private final String cid = "TC0ONETIME";
    private final String KAKAO_PAY_API_URL = "https://kapi.kakao.com/v1/payment/ready";
    private final String KAKAO_API_KEY = "c415f557213096780ca7e9e9480e864b";
    private final MenuRepository menuRepository;

    public KakaoPayService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }



    public String preparePayment(ReservationCreateResponseDto reservation) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

        Map<Long, Long> menus = reservation.extractMenuIdAndQuantities();
        long total = 0L;

        // 메뉴의 가격과 수량을 이용하여 총액 계산
        for (Map.Entry<Long, Long> entry : menus.entrySet()) {
            try {
                Menu menu = menuRepository.findByIdOrElseThrow(entry.getKey());
                total += menu.getMenuPrice() * entry.getValue();
            } catch (NoSuchElementException e) {
                throw new RuntimeException("메뉴 ID " + entry.getKey() + "에 대한 정보를 찾을 수 없습니다.", e);
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", reservation.getReservationId().toString());
        params.put("partner_user_id", reservation.getUserId().toString());
        params.put("item_name", "Reservation Payment");
        params.put("quantity", "1");
        params.put("total_amount", String.valueOf(total));
        params.put("vat_amount", "200");
        params.put("tax_free_amount", "0");
        params.put("approval_url", "http://localhost:8080/payment/success");
        params.put("cancel_url", "http://localhost:8080/payment/cancel");
        params.put("fail_url", "http://localhost:8080/payment/fail");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            // 카카오페이 API에 POST 요청 보내기
            ResponseEntity<Map> response = restTemplate.postForEntity(KAKAO_PAY_API_URL, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // 응답 본문에서 결제 준비 URL을 반환
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("next_redirect_pc_url")) {
                    return (String) responseBody.get("next_redirect_pc_url");
                } else {
                    throw new RuntimeException("카카오페이 결제 준비 URL을 찾을 수 없습니다.");
                }
            } else {
                throw new RuntimeException("카카오페이 결제 준비 실패, 상태 코드: " + response.getStatusCode());
            }
        } catch (HttpStatusCodeException e) {
            // HTTP 상태 코드에 따른 예외 처리
            throw new RuntimeException("카카오페이 API 호출 실패, 상태 코드: " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            // REST 클라이언트 예외 처리 (네트워크 오류 등)
            throw new RuntimeException("카카오페이 결제 준비 중 네트워크 오류 발생", e);
        } catch (Exception e) {
            // 기타 예외 처리
            throw new RuntimeException("결제 준비 중 오류 발생", e);
        }
    }

    public void approvePayment(String tid, String pgToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", tid);
        params.put("partner_order_id","order_id");
        params.put("partner_user_id","user_id");
        params.put("pg_token", pgToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(KAKAO_PAY_API_URL, entity, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오페이 결제 승인 실패");
        }
    }


}
