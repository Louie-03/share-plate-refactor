package louie.hanse.shareplate.core.notification.event.activity.listener;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.entry.event.EntryCancelEvent;
import louie.hanse.shareplate.core.entry.event.EntryEvent;
import louie.hanse.shareplate.core.notification.domain.ActivityType;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import louie.hanse.shareplate.core.share.event.ShareCancelEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivityNotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void registerEntry(EntryEvent event) {
        notificationService.saveActivityNotificationAndSend(
            event.getShareId(), event.getMemberId(), ActivityType.ENTRY);
    }

    @EventListener
    public void registerEntryCancel(EntryCancelEvent event) {
        notificationService.saveActivityNotificationAndSend(
            event.getShareId(), event.getMemberId(), ActivityType.ENTRY_CANCEL);
    }

    @EventListener
    public void registerShareCancel(ShareCancelEvent event) {
        notificationService.saveActivityNotificationAndSend(
            event.getShareId(), event.getMemberId(), ActivityType.SHARE_CANCEL);
    }

}

