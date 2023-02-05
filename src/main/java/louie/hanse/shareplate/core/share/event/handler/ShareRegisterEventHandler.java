package louie.hanse.shareplate.core.share.event.handler;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.notification.event.ActivityNotificationRegisterEvent;
import louie.hanse.shareplate.core.notification.event.ShareRegisterEvent;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShareRegisterEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void registerKeywordNotification(ShareRegisterEvent event) {
        notificationService.saveKeywordNotificationAndSend(event.getShareId(), event.getMemberId());
    }

    @EventListener
    public void registerActivityNotification(ActivityNotificationRegisterEvent event) {
        notificationService.saveActivityNotificationAndSend(event.getShareId(), event.getMemberId(),
            event.getActivityType());
    }
}
