package com.gotcha.earlytable.domain.waiting.dto;

import com.gotcha.earlytable.domain.file.entity.FileDetail;
import com.gotcha.earlytable.domain.file.enums.FileStatus;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import com.gotcha.earlytable.global.enums.WaitingStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WaitingGetOneResponseDto {
    private final Long waitingId;
    private final Long storeId;
    private final Long waitingNumber;
    private final String storeName;
    private final WaitingStatus waitingStatus;
    private final String waitingType;
    private final int personnelCount;
    private List<HashMap<String, Object>>  partyPeople = new ArrayList<>();
    private final String phone;

    public WaitingGetOneResponseDto(Waiting waiting) {
        this.waitingId = waiting.getWaitingId();
        this.waitingNumber = waiting.getWaitingNumber();
        this.storeName = waiting.getStore().getStoreName();
        this.waitingStatus = waiting.getWaitingStatus();
        this.waitingType = waiting.getWaitingType().getValue();
        this.personnelCount = waiting.getPersonnelCount();
        this.phone = waiting.getPhone();
        this.storeId = waiting.getStore().getStoreId();

        if (waiting.getParty() != null) {
            this.partyPeople = waiting.getParty().getPartyPeople().stream()
                    .map(partyPeople -> {
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("userName", partyPeople.getUser().getNickName());
                        userMap.put("userId", partyPeople.getUser().getId());
                        userMap.put("userImage",partyPeople.getUser().getFile().getFileDetailList().stream()
                                .filter(fileDetail->fileDetail.getFileStatus().equals(FileStatus.REPRESENTATIVE)).map(FileDetail::getFileUrl).findFirst().orElse(null));
                        return userMap;
                    })
                    .collect(Collectors.toList());

        }
    }
}
