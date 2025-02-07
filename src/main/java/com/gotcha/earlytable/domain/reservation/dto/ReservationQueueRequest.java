package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ReservationQueueRequest {
    private Long storeId;               // 가게 ID
    private ReservationCreateRequestDto requestDto;  // 예약 요청 데이터
    private User user;                  // 예약을 요청한 사용자

    // 생성자
    public ReservationQueueRequest(Long storeId, ReservationCreateRequestDto requestDto, User user) {
        this.storeId = storeId;
        this.requestDto = requestDto;
        this.user = user;
    }


}
