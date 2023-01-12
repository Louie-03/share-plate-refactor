package louie.hanse.shareplate.core.notification.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.NotificationExceptionType;
import louie.hanse.shareplate.core.share.domain.Share;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private LocalDateTime createdDateTime = LocalDateTime.now();

    public Notification(Share share, Member member, NotificationType type) {
        this.share = share;
        this.member = member;
        this.type = type;
    }

    public void isNotMemberThrowException(Member member) {
        if (!this.member.equals(member)) {
            throw new GlobalException(NotificationExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION);
        }
    }
}
