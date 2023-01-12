package louie.hanse.shareplate.core.notification.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.notification.domain.ActivityType;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;

@NoArgsConstructor
@Getter
public class ActivityNotificationResponse {

    private Long id;
    private String recruitmentMemberNickname;
    private LocalDateTime notificationCreatedDateTime;
    private String shareTitle;
    private String shareThumbnailImageUrl;
    private Long shareId;
    private Long writerId;
    private ActivityType activityType;

    public ActivityNotificationResponse(ActivityNotification activityNotification) {
        this.id = activityNotification.getId();
        this.recruitmentMemberNickname = activityNotification.isDeadLine() ?
            null : activityNotification.getEntryMember().getNickname();
        this.notificationCreatedDateTime = activityNotification.getCreatedDateTime();
        this.shareTitle = activityNotification.getShare().getTitle();
        this.shareThumbnailImageUrl = activityNotification.getShare().getShareImages().get(0)
            .getImageUrl();
        this.shareId = activityNotification.getShare().getId();
        this.writerId = activityNotification.getShare().getWriter().getId();
        this.activityType = activityNotification.getActivityType();
    }
}
