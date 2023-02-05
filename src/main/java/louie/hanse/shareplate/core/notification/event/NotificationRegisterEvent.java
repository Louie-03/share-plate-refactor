package louie.hanse.shareplate.core.notification.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationRegisterEvent {

    private final Long shareId;
    private final Long memberId;
}
