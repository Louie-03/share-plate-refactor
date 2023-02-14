package louie.hanse.shareplate.core.notification.event.activity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ActivityNotificationsSaveEvent {

    private List<Long> activityNotificationIds;
    private List<Long> entryIds;

}
