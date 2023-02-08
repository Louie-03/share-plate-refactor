package louie.hanse.shareplate.core.notification.event.activity;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityNotificationsSaveEvent {

    private final List<Long> activityNotificationIds;
    private final List<Long> entryIds;

}
