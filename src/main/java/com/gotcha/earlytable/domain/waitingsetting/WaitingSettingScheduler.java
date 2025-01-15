package com.gotcha.earlytable.domain.waitingsetting;

import com.gotcha.earlytable.domain.waitingsetting.entity.WaitingSetting;
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

    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void updateWaitingSettingStatus() {
        List<WaitingSetting> waitingSettings = waitingSettingRepository.findAll();
        for (WaitingSetting waitingSetting : waitingSettings) {
            waitingSettingService.updateWaitingSettingStatus(waitingSetting.getWaitingSettingId());
        }
    }
}
