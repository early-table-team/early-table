package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.domain.menu.entity.Menu;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ReservationCreateResponseDto {

    private final Long userId;

    private final Long reservationId;

    private final LocalDate reservationDate;

    private final LocalTime reservationTime;

    private final Integer personnelCount;

    private final List<ReturnMenuListDto> menuList;

    private  String paymentUrl;

    private String tid;

    public ReservationCreateResponseDto(Long userId, Long reservationId, LocalDate reservation, LocalTime reservationTime, Integer personnelCount, List<ReturnMenuListDto> menuList) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.reservationDate = reservation;
        this.reservationTime = reservationTime;
        this.personnelCount = personnelCount;
        this.menuList = menuList;
        this.paymentUrl = null;

    }

    public List<Long> extractMenuIds() {
        return menuList.stream()
                .map(ReturnMenuListDto::getMenuId)
                .collect(Collectors.toList());
    }

    public Map<Long, Long> extractMenuIdAndQuantities() {
        return menuList.stream()
                .collect(Collectors.toMap(
                        ReturnMenuListDto::getMenuId,
                        ReturnMenuListDto::getMenuCount
                ));
    }


    public void setPaymentUrl(String paymentRedirectUrl) {
        this.paymentUrl = paymentRedirectUrl;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

}
