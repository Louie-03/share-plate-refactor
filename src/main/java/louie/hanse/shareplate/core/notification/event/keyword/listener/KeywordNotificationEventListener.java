package louie.hanse.shareplate.core.notification.event.keyword.listener;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import louie.hanse.shareplate.core.share.event.ShareRegisterEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeywordNotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void registerKeyword(ShareRegisterEvent event) {
        notificationService.saveKeywordNotificationAndSend(event.getShareId(), event.getMemberId());
    }

}

