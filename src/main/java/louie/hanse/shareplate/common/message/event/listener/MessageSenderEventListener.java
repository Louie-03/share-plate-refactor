package louie.hanse.shareplate.common.message.event.listener;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.message.sender.MessageSender;
import louie.hanse.shareplate.core.chat.event.ChatSaveEvent;
import louie.hanse.shareplate.core.notification.event.activity.ActivityNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.event.keyword.KeywordNotificationsSaveEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageSenderEventListener {

    private final MessageSender messageSender;

    @KafkaListener(topics = "activity-notifications-save")
    public void sendActivityNotifications(ActivityNotificationsSaveEvent event) {
        messageSender.sendActivityNotifications(event.getActivityNotificationIds(),
            event.getEntryIds());
    }

    @KafkaListener(topics = "keyword-notifications-save")
    public void sendKeywordNotifications(KeywordNotificationsSaveEvent event) {
        messageSender.sendKeywordNotifications(event.getKeywordNotificationIds(),
            event.getKeywordIds());
    }

    @KafkaListener(topics = "chat-save")
    public void sendChatDetail(ChatSaveEvent event) {
        messageSender.sendChatDetail(event.getChatId());
    }

}
