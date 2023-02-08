package louie.hanse.shareplate.core.notification.repository;

import java.util.List;
import java.util.Optional;
import louie.hanse.shareplate.core.notification.domain.Notification;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select an from ActivityNotification an where "
        + "an.member.id = :memberId and an.type = 'ACTIVITY'")
    List<ActivityNotification> findAllActivityNotificationByMemberId(
        @Param("memberId") Long memberId);

    @Query("select n from Notification n where "
        + "n.member.id = :memberId and n.type = 'KEYWORD'")
    List<Notification> findAllKeywordNotificationByMemberId(@Param("memberId") Long memberId);

    @Query("select n from Notification n join fetch n.member where n.id = :id")
    Optional<Notification> findWithMemberById(@Param("id") Long id);

    @Query("select an from ActivityNotification an where an.id = :id")
    Optional<ActivityNotification> findActivityNotificationById(@Param("id") Long id);

    @Query("select n from Notification n where n.id = :id and n.type = 'KEYWORD'")
    Optional<Notification> findKeywordNotificationById(@Param("id") Long id);
}
