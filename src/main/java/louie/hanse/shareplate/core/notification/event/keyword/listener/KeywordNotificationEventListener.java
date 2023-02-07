package louie.hanse.shareplate.core.notification.event.keyword.listener;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import louie.hanse.shareplate.core.share.event.ShareRegisterEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class KeywordNotificationEventListener {

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener
    public void registerKeyword(ShareRegisterEvent event) {
        notificationService.saveKeywordNotificationAndSend(event.getShareId(), event.getMemberId());
    }

}

