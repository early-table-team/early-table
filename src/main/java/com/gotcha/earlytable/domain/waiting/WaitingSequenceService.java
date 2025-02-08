package com.gotcha.earlytable.domain.waiting;

import com.gotcha.earlytable.domain.waiting.dto.WaitingSequenceDto;
import com.gotcha.earlytable.domain.waiting.entity.Waiting;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class WaitingSequenceService {

    private final RedissonClient redissonClient;
    private final WaitingRepository waitingRepository;

    public WaitingSequenceService(RedissonClient redissonClient, WaitingRepository waitingRepository) {
        this.redissonClient = redissonClient;
        this.waitingRepository = waitingRepository;
    }

    /**
     * 웨이팅 큐에 등록하는 메서드
     *
     * @param waiting
     */
    public void addToWaitingQueue(Waiting waiting) {
        String key = "waiting:store:" + waiting.getWaitingType().name() + ":" + waiting.getStore().getStoreId();
        RScoredSortedSet<Long> waitingQueue = redissonClient.getScoredSortedSet(key);

        // 웨이팅 번호를 score 로 추가 (혹시라도 있을 중복을 위해 날짜로 번호 구분)
        waitingQueue.add(LocalDate.now().getDayOfYear() * 1000000L + waiting.getWaitingNumber(), waiting.getWaitingId());


        // TTL 설정 (6시간 후 자동 삭제)
        waitingQueue.expire(Duration.ofHours(6));
    }

    /**
     * 웨이팅 현재 내 순서 가져오는 메서드
     *
     * @param waiting
     * @return 현재 내 순서
     */
    public Integer getNowSeqNumber(Waiting waiting) {
        String key = "waiting:store:" + waiting.getWaitingType() + ":" + waiting.getStore().getStoreId();
        RScoredSortedSet<Long> waitingQueue = redissonClient.getScoredSortedSet(key);

        // 현재 웨이팅의 번호 조회
        Integer rank = waitingQueue.rank(waiting.getWaitingId());
        if (rank == null) {
            return 0;
        }


        // 현재 웨이팅 번호보다 작은 웨이팅의 수 계산
        return rank + 1;
    }

    /**
     * 웨이팅 큐에서 제거하는 메서드
     *
     * @param waiting
     */
    public void removeFromWaitingQueue(Waiting waiting) {
        String key = "waiting:store:" + waiting.getWaitingType() + ":" + waiting.getStore().getStoreId();
        RScoredSortedSet<Long> waitingQueue = redissonClient.getScoredSortedSet(key);

        // Redis 의 대기열에서 해당 웨이팅 ID 제거
        waitingQueue.remove(waiting.getWaitingId());

    }

    /**
     * 웨이팅 등록 시 몇 팀 남았는지 저장하는 메서드
     *
     * @param waiting
     */
    public void saveWaitingLeft(Waiting waiting) {
        String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":left";

        RMap<Long, Integer> waitingLeftMap = redissonClient.getMap(key);

        Integer leftNow = getNowSeqNumber(waiting);

        waitingLeftMap.put(waiting.getWaitingNumber(), leftNow);
    }

    /**
     * 웨이팅 1팀 당 소요시간 저장하는 메서드
     *
     * @param waitingId
     */
    public void saveTakenTimeWaiting(Long waitingId) {
        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        Integer takenTime = (int) Duration.between(waiting.getCreatedAt(), waiting.getModifiedAt()).toMinutes(); // 등록 - 입장 소요시간

        String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":time";

        RScoredSortedSet<Long> timeQueue = redissonClient.getScoredSortedSet(key);

        String leftKey = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":left";

        RMap<Long, Integer> waitingLeftMap = redissonClient.getMap(leftKey);

        Integer left = waitingLeftMap.get(waiting.getWaitingId());

        timeQueue.add((long) (takenTime / left), waiting.getWaitingId()); // 소요시간 / 등록 시 대기 팀 수 = 1팀 당 소요시간 , 웨이팅 아이디

        if (timeQueue.size() > 150) {
            timeQueue.remove(0);
        }
    }

    /**
     * 저장된 소요시간 삭제하는 메서드
     *
     * @param waiting
     */
    public void deleteTakenTimeWaiting(Waiting waiting) {
        String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":time";

        RScoredSortedSet<Long> timeQueue = redissonClient.getScoredSortedSet(key);

        timeQueue.remove(waiting.getWaitingId());

    }


    /**
     * 에상 대기시간 조회 메서드
     *
     * @param waiting
     * @return Integer
     */
    public Integer getTakenTimeWaiting(Waiting waiting) {
        String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":time";

        RScoredSortedSet<Long> timeQueue = redissonClient.getScoredSortedSet(key);

        long leftNow = getNowSeqNumber(waiting);

        int sum = 0;
        int time;

        if (timeQueue.size() < 10) {
            time = 15;
        } else {
            for (Long num : timeQueue) {
                sum += num.intValue();
            }
            time = sum / timeQueue.size();
        }

        return time * (int) leftNow;

    }

    /**
     * 현재 나의 순서와 대기 예상 시간 조회 메서드
     *
     * @param waitingId
     * @return WaitingSequenceDto
     */
    public WaitingSequenceDto getNowSequenceAndTime(Long waitingId) {
        Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

        // 현재 나의 순서 조회
        Integer nowSeqNumber = getNowSeqNumber(waiting);

        // 예상 대기 시간 조회
        Integer waitingTime = getTakenTimeWaiting(waiting);

        return new WaitingSequenceDto(nowSeqNumber, waitingTime);
    }
}
