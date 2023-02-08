package louie.hanse.shareplate.core.notification.event.activity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityNotificationSaveEvent {

    private final Long activityNotificationId;
    private final Long entryId;
}
