package louie.hanse.shareplate.common.kafka;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chat.event.ChatSaveEvent;
import louie.hanse.shareplate.core.notification.event.activity.ActivityNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.event.keyword.KeywordNotificationsSaveEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    @TransactionalEventListener
    public void sendActivityNotificationsSaveEvent(ActivityNotificationsSaveEvent event) {
        kafkaTemplate.send("activity-notifications-save", event);
    }

    @Async
    @TransactionalEventListener
    public void sendKeywordNotificationsSaveEvent(KeywordNotificationsSaveEvent event) {
        kafkaTemplate.send("keyword-notifications-save", event);
    }

    @Async
    @TransactionalEventListener
    public void sendChatSaveEvent(ChatSaveEvent event) {
        kafkaTemplate.send("chat-save", event);
    }

}
