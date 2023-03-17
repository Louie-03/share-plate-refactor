package louie.hanse.shareplate.common.message;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.chat.event.ChatSaveEvent;
import louie.hanse.shareplate.core.notification.event.activity.ActivityNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.event.keyword.KeywordNotificationsSaveEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Async
    @TransactionalEventListener
    public void sendActivityNotificationsSaveEvent(ActivityNotificationsSaveEvent event) {
        convertAndSendFanoutExchange("activity-notifications-save", event);
    }

    @Async
    @TransactionalEventListener
    public void sendKeywordNotificationsSaveEvent(KeywordNotificationsSaveEvent event) {
        convertAndSendFanoutExchange("keyword-notifications-save", event);
    }

    @Async
    @TransactionalEventListener
    public void sendChatSaveEvent(ChatSaveEvent event) {
        convertAndSendFanoutExchange("chat-save", event);
    }

    private void convertAndSendFanoutExchange(String exchange, Object event) {
        rabbitTemplate.convertAndSend(exchange, "", event);
    }

}
