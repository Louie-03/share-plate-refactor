package louie.hanse.shareplate.core.notification.event.keyword.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import louie.hanse.shareplate.core.share.event.ShareRegisterEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeywordNotificationEventListener {

    private final NotificationService notificationService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    @Async
    public void registerKeyword(ShareRegisterEvent event) {
        log.info("registerKeyword");
        notificationService.saveKeywordNotification(event.getShareId(), event.getMemberId());
        throw new RuntimeException();
    }

}

