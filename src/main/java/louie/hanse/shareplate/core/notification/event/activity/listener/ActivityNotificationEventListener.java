package louie.hanse.shareplate.core.notification.event.activity.listener;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.entry.event.EntryCancelEvent;
import louie.hanse.shareplate.core.entry.event.EntryEvent;
import louie.hanse.shareplate.core.notification.domain.ActivityType;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import louie.hanse.shareplate.core.share.event.ShareCancelEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ActivityNotificationEventListener {

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener
    public void registerEntry(EntryEvent event) {
        notificationService.saveActivityNotificationAndSend(
            event.getShareId(), event.getMemberId(), ActivityType.ENTRY);
    }

    @Async
    @TransactionalEventListener
    public void registerEntryCancel(EntryCancelEvent event) {
        notificationService.saveActivityNotificationAndSend(
            event.getShareId(), event.getMemberId(), ActivityType.ENTRY_CANCEL);
    }

    @Async
    @TransactionalEventListener
    public void registerShareCancel(ShareCancelEvent event) {
        notificationService.saveActivityNotificationAndSend(
            event.getShareId(), event.getMemberId(), ActivityType.SHARE_CANCEL);
    }

}

