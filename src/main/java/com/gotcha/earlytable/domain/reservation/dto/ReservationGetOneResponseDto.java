package com.gotcha.earlytable.domain.reservation.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.reservation.entity.Reservation;
import com.gotcha.earlytable.global.enums.ReservationStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReservationGetOneResponseDto {

    private final Long reservationId;

    private final Long storeId;

    private final LocalDateTime reservationDate;

    private final String storeName;

    private final ReservationStatus reservationStatus;

    private final Integer personnelCount;

    private final List<HashMap<String, Object>> invitationPeople;

    private final List<ReturnMenuListDto> menuList;

    private final boolean isExist;

    public ReservationGetOneResponseDto(Reservation reservation, List<ReturnMenuListDto> menuList, boolean isExist) {
        this.reservationId = reservation.getReservationId();
        this.reservationDate = reservation.getReservationDate().atTime(reservation.getReservationTime()); // 예약에 저장된 날짜와 시간을 조합하여 한번에 보여줌
        this.storeName = reservation.getStore().getStoreName();
        this.reservationStatus = reservation.getReservationStatus();
        this.personnelCount = reservation.getPersonnelCount();
        this.invitationPeople = reservation.getParty().getPartyPeople().stream()
                .map(partyPeople -> {
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("userName", partyPeople.getUser().getNickName());
                    userMap.put("userId", partyPeople.getUser().getId());
                    userMap.put("userImage",partyPeople.getUser().getFile().getFileDetailList().stream()
                            .filter(fileDetail->fileDetail.getFileStatus().equals(FileStatus.REPRESENTATIVE)).map(FileDetail::getFileUrl).findFirst().orElse(null));
                    return userMap;
                })
                .collect(Collectors.toList());
        this.storeId = reservation.getStore().getStoreId();
        this.menuList = menuList;
        this.isExist = isExist;
    }
}
