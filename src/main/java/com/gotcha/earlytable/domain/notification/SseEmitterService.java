package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.domain.notification.entity.Notification;
import com.gotcha.earlytable.domain.store.storeView.StoreViewRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.NotificationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Set;

@Service
public class SseEmitterService {

    // SSE 이벤트 타임아웃 시간
    private static final Long DEFAULT_TIMEOUT = 60000000L;
    private static final String NOTIFICATION_PREFIX = "notification:user:";


    private final SseEmitterRepository sseEmitterRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationDao notificationDao;
    private final NotificationPublisher notificationPublisher;
    private final StoreViewRepository storeViewRepository;

    public SseEmitterService(SseEmitterRepository sseEmitterRepository, NotificationRepository notificationRepository,
                             NotificationDao notificationDao, NotificationPublisher notificationPublisher,
                             StoreViewRepository storeViewRepository) {

        this.sseEmitterRepository = sseEmitterRepository;
        this.notificationRepository = notificationRepository;
        this.notificationDao = notificationDao;
        this.notificationPublisher = notificationPublisher;
        this.storeViewRepository = storeViewRepository;
    }

    /**
     * 클라이언트의 이벤트 구독을 허용하는 메서드
     */
    @Transactional
    public SseEmitter subscribe(Long userId) {
        // sse의 유효 시간이 만료되면, 클라이언트에서 다시 서버로 이벤트 구독을 시도한다.
        SseEmitter sseEmitter = sseEmitterRepository.save(userId, new SseEmitter(DEFAULT_TIMEOUT));


        // 사용자에게 모든 데이터가 전송되었다면 emitter 삭제
        sseEmitter.onCompletion(() -> sseEmitterRepository.deleteById(userId));

        // Emitter의 유효 시간이 만료되면 emitter 삭제
        // 유효 시간이 만료되었다는 것은 클라이언트와 서버가 연결된 시간동안 아무런 이벤트가 발생하지 않은 것을 의미한다.
        sseEmitter.onTimeout(() -> sseEmitterRepository.deleteById(userId));

        // 첫 구독시에 이벤트를 발생시킨다.
        // sse 연결이 이루어진 후, 하나의 데이터로 전송되지 않는다면 sse의 유효 시간이 만료되고 503 에러가 발생한다.
        sendToClient(userId, "subscribe event, userId : " + userId, NotificationType.INIT);

        // 접속 x 일 동안 온 알림 가져오기
        List<Notification> notifications = notificationDao.getValuesForNotification(NOTIFICATION_PREFIX + userId);

        // 없으면 그냥 emitter 전달하고 있으면 알림 보내기
        if(notifications.isEmpty()) {
            notificationDao.setValuesForNotification(NOTIFICATION_PREFIX + userId);

            return sseEmitter;
        }

        // 있으면 알림 보내기
        for (Notification notification : notifications) {
            sendToClient(userId, notification.getContent(), notification.getType());
            notification.read();
        }

        // 캐시 비우기
        notificationDao.deleteValuesForNotification(NOTIFICATION_PREFIX + userId);


        return sseEmitter;
    }

    /**
     * 실제로 다른 곳에서 알림 보낼 때 사용하는 메서드
     *
     * @param user
     * @param content
     */
    @Transactional
    public void send(User user, String content, NotificationType notificationType) {
        Notification notification = new Notification(user.getId(), content, notificationType);

        notificationRepository.save(notification);

        // Emitter의 존재 여부를 확인하고,
        // Emitter 존재 시 notification 발송
        checkEmitterAndSendToClient(user.getId(), notification);

    }

    /**
     * Emitter 존재 체크 및 존재 여부에 따라 true: 발송 or false: redis 에 저장
     * Emitter 가 존재한다는건 현재 로그인된 상태임
     *
     * @param userId
     * @param notification
     */
    @Transactional
    public void checkEmitterAndSendToClient(Long userId, Notification notification){

        if(sseEmitterRepository.existsById(userId)){
            sendToClient(userId, notification.getContent(), notification.getType());
            notification.read();
            notificationRepository.save(notification);
            return;
        }

        notificationDao.updateValuesForNotification(NOTIFICATION_PREFIX + userId, notification);
    }

    /**
     * 클라이언트에게 알림 발송하는 메서드
     *
     * @param userId
     * @param data
     * @param notificationType
     */
    private void sendToClient(Long userId, Object data, NotificationType notificationType) {

        notificationPublisher.publishNotification(userId, data, notificationType);
    }

    /**
     * 특정 가게를 조회중인 클라이언트에게 조회수 알림을 전송하는 메서드
     *
     * @param storeId
     * @param count
     */
    public void sendStoreViewNotification(Long storeId, Long count) {

        Set<Long> userIds = storeViewRepository.findUserIdsByStoreId(storeId); // storeId로 연결된 사용자 ID를 찾는 로직

        for (Long userId : userIds) {
            sendToClient(userId, count, NotificationType.STORE_VIEW);
        }
    }
}
