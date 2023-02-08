package louie.hanse.shareplate.common.message.sender;

import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import louie.hanse.shareplate.core.notification.domain.Notification;
import louie.hanse.shareplate.core.notification.dto.response.ActivityNotificationResponse;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageSender {

    private final MessageSendingOperations messageSendingOperations;

    public void sendActivityNotifications(List<ActivityNotification> activityNotifications,
        List<Long> entryIds) {

        for (int i = 0; i < activityNotifications.size(); i++) {
            String destination = "/queue/notifications/entries/" + entryIds.get(i);

            messageSendingOperations.convertAndSend(destination,
                new ActivityNotificationResponse(activityNotifications.get(i)));
        }
    }

    public void sendKeywordNotifications(List<Notification> keywordNotifications,
        List<Long> keywordIds) {

        for (int i = 0; i < keywordNotifications.size(); i++) {
            String destination = "/queue/notifications/keywords/" + keywordIds.get(i);

            messageSendingOperations.convertAndSend(destination,
                new KeywordNotificationResponse(keywordNotifications.get(i)));
        }
    }
}
