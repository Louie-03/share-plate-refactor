package louie.hanse.shareplate.core.notification.event.activity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.core.notification.domain.ActivityType;

@Getter
@RequiredArgsConstructor
public class ActivityNotificationRegisterEvent {

    private final Long shareId;
    private final Long memberId;
    private final ActivityType activityType;
}
