package com.gotcha.earlytable.domain.reservation;

import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateRequestDto;
import com.gotcha.earlytable.domain.reservation.dto.ReservationCreateResponseDto;
import com.gotcha.earlytable.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationCreateResponseDto createReservation(Long storeId, ReservationCreateRequestDto requestDto, User user) {

        // TODO : 해당 가게가 존재 하는가?

        // TODO : 가게 예약 타입이 예약이 맞는가? -> 예약이 가능한지

        // TODO : 휴무날짜는 아닌가?

        // TODO : 해당 요일의 영업시간 내에 충족하는가?

        // TODO : 받아온 메뉴 리스트가 해당 가게 안에 모두 있는가?

        // TODO : 인원수에 해당하는 자리가 남아 있는가?  -> 예약 불가 : 전화 문의하기

        // TODO : OK 그럼 예약 생성해줄게

        // TODO : 남은 자리수 업데이트 하기


    }


}
