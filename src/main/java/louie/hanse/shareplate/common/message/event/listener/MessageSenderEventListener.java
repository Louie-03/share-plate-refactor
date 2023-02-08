package louie.hanse.shareplate.common.message.event.listener;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.message.sender.MessageSender;
import louie.hanse.shareplate.core.chat.event.ChatSaveEvent;
import louie.hanse.shareplate.core.notification.event.activity.ActivityNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.event.keyword.KeywordNotificationsSaveEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class MessageSenderEventListener {

    private final MessageSender messageSender;

    @Async
    @TransactionalEventListener
    public void sendActivityNotifications(ActivityNotificationsSaveEvent event) {
        messageSender.sendActivityNotifications(event.getActivityNotificationIds(),
            event.getEntryIds());
    }

    @Async
    @TransactionalEventListener
    public void sendKeywordNotifications(KeywordNotificationsSaveEvent event) {
        messageSender.sendKeywordNotifications(event.getKeywordNotificationIds(),
            event.getKeywordIds());
    }

    @Async
    @TransactionalEventListener
    public void sendChatDetail(ChatSaveEvent event) {
        messageSender.sendChatDetail(event.getChatId());
    }

}
