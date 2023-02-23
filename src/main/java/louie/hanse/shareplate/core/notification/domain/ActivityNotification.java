package louie.hanse.shareplate.core.notification.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.share.domain.Share;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityNotification extends Notification {

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member entryMember;

    public ActivityNotification(Share share, Member member,
        NotificationType type, ActivityType activityType, Member entryMember) {
        super(share, member, type);
        this.activityType = activityType;
        this.entryMember = entryMember;
    }

    public ActivityNotification(Share share, Member member, NotificationType type,
        ActivityType activityType) {
        this(share, member, type, activityType, null);
    }

    public boolean isDeadLine() {
        return activityType.isDeadLine();
    }
}
