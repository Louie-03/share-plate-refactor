package louie.hanse.shareplate.common.message.sender;

import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import louie.hanse.shareplate.core.notification.domain.Notification;
import louie.hanse.shareplate.core.notification.dto.response.ActivityNotificationResponse;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MessageSender {

    private final MessageSendingOperations messageSendingOperations;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public void sendActivityNotifications(List<Long> activityNotificationIds, List<Long> entryIds) {

        for (int i = 0; i < activityNotificationIds.size(); i++) {
            ActivityNotification activityNotification = notificationService
                .findActivityNotificationByIdOrElseThrow(activityNotificationIds.get(i));

            String destination = "/queue/notifications/entries/" + entryIds.get(i);

            messageSendingOperations.convertAndSend(destination,
                new ActivityNotificationResponse(activityNotification));
        }
    }

    @Transactional(readOnly = true)
    public void sendKeywordNotifications(List<Long> keywordNotificationIds, List<Long> keywordIds) {

        for (int i = 0; i < keywordNotificationIds.size(); i++) {
            Notification notification = notificationService
                .findKeywordNotificationByIdOrElseThrow(keywordNotificationIds.get(i));

            String destination = "/queue/notifications/keywords/" + keywordIds.get(i);

            messageSendingOperations.convertAndSend(destination,
                new KeywordNotificationResponse(notification));
        }
    }
}
