package louie.hanse.shareplate.core.notification.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.notification.domain.Notification;

@NoArgsConstructor
@Getter
public class KeywordNotificationResponse {

    private Long id;
    private String shareLocation;
    private Long shareId;
    private Long writerId;
    private String shareTitle;
    private String shareThumbnailImageUrl;
    private LocalDateTime notificationCreatedDateTime;

    public KeywordNotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.shareLocation = notification.getShare().getLocation();
        this.shareId = notification.getShare().getId();
        this.writerId = notification.getShare().getWriter().getId();
        this.shareTitle = notification.getShare().getTitle();
        this.shareThumbnailImageUrl = notification.getShare().getShareImages().get(0).getImageUrl();
        this.notificationCreatedDateTime = notification.getCreatedDateTime();
    }
}
