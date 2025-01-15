package com.gotcha.earlytable.domain.waitingsetting;

import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
import com.gotcha.earlytable.domain.waitingsetting.enums.WaitingSettingStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class WaitingSettingScheduler {
    private final WaitingSettingService waitingSettingService;
    private final WaitingSettingRepository waitingSettingRepository;

    public WaitingSettingScheduler(WaitingSettingService waitingSettingService, WaitingSettingRepository waitingSettingRepository) {
        this.waitingSettingService = waitingSettingService;
        this.waitingSettingRepository = waitingSettingRepository;
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void updateWaitingSettingStatus() {
        List<WaitingSetting> waitingSettings = waitingSettingRepository.findAllByWaitingSettingStatus(WaitingSettingStatus.CLOSE);
        for (WaitingSetting waitingSetting : waitingSettings) {
            waitingSettingService.updateWaitingSettingStatus(waitingSetting.getWaitingSettingId());
        }
    }
}
