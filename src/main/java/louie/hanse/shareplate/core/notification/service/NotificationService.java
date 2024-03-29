package louie.hanse.shareplate.core.notification.service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.exception.GlobalException;
import louie.hanse.shareplate.common.exception.type.NotificationExceptionType;
import louie.hanse.shareplate.core.entry.domain.Entry;
import louie.hanse.shareplate.core.entry.repository.EntryRepository;
import louie.hanse.shareplate.core.keyword.domain.Keyword;
import louie.hanse.shareplate.core.keyword.repository.KeywordRepository;
import louie.hanse.shareplate.core.member.domain.Member;
import louie.hanse.shareplate.core.member.service.MemberService;
import louie.hanse.shareplate.core.notification.domain.ActivityNotification;
import louie.hanse.shareplate.core.notification.domain.ActivityType;
import louie.hanse.shareplate.core.notification.domain.Notification;
import louie.hanse.shareplate.core.notification.domain.NotificationType;
import louie.hanse.shareplate.core.notification.dto.response.ActivityNotificationResponse;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import louie.hanse.shareplate.core.notification.event.activity.ActivityNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.event.keyword.KeywordNotificationsSaveEvent;
import louie.hanse.shareplate.core.notification.repository.NotificationRepository;
import louie.hanse.shareplate.core.share.domain.Share;
import louie.hanse.shareplate.core.share.service.ShareService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private static final ZoneOffset seoulZoneOffset = ZoneOffset.of("+9");

    private final TaskScheduler taskScheduler;
    private final EntryRepository entryRepository;
    private final NotificationRepository notificationRepository;
    private final KeywordRepository keywordRepository;
    private final ShareService shareService;
    private final MemberService memberService;
    private final ApplicationEventPublisher eventPublisher;

    public List<ActivityNotificationResponse> getActivityNotificationList(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        List<ActivityNotification> activityNotifications = notificationRepository
            .findAllActivityNotificationByMemberId(memberId);

        return activityNotifications.stream()
            .map(ActivityNotificationResponse::new)
            .collect(Collectors.toList());
    }

    public List<KeywordNotificationResponse> getKeywordNotificationList(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        List<Notification> keywordNotifications = notificationRepository
            .findAllKeywordNotificationByMemberId(memberId);

        return keywordNotifications.stream()
            .map(KeywordNotificationResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Notification notification = findWithMemberByIdOrElseThrow(id);
        notification.isNotMemberThrowException(member);
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(List<Long> idList, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        for (Long id : idList) {
            Notification notification = findWithMemberByIdOrElseThrow(id);
            notification.isNotMemberThrowException(member);
        }
        notificationRepository.deleteAllByIdInBatch(idList);
    }

    @Transactional
    public void saveKeywordNotification(Long shareId, Long memberId) {
        Share share = shareService.findByIdOrElseThrow(shareId);
        List<Keyword> keywords = keywordRepository.findAllByContainsContentsAndNotMemberIdAndAroundShare(
            memberId, share.getTitle(), share.getLongitude(), share.getLatitude());

        List<Notification> notifications = new ArrayList<>();
        for (Keyword keyword : keywords) {
            Notification notification = new Notification(
                share, keyword.getMember(), NotificationType.KEYWORD);
            notifications.add(notification);
        }
        notificationRepository.saveAll(notifications);

        sendKeywordNotifications(keywords, notifications);

        createDeadlineNotificationSchedule(share.getId());
    }

    @Transactional
    public void saveActivityNotification(Long shareId, Long memberId,
        ActivityType activityType) {

        Share share = shareService.findByIdOrElseThrow(shareId);
        List<Entry> entries;
        if (activityType.isDeadLine()) {
            entries = entryRepository.findAllByShareId(shareId);
        } else {
            entries = entryRepository.findAllByShareIdAndNotMemberId(shareId, memberId);
        }

        List<ActivityNotification> activityNotifications = new ArrayList<>();
        for (Entry entry : entries) {
            ActivityNotification activityNotification;
            if (activityType.isDeadLine()) {
                activityNotification = new ActivityNotification(
                    share, entry.getMember(), NotificationType.ACTIVITY, ActivityType.DEADLINE);
            } else {
                Member member = memberService.findByIdOrElseThrow(memberId);

                activityNotification = new ActivityNotification(share,
                    entry.getMember(), NotificationType.ACTIVITY, activityType, member);
            }
            activityNotifications.add(activityNotification);
        }
        notificationRepository.saveAll(activityNotifications);

        List<Long> notificationIds = activityNotifications.stream()
            .map(Notification::getId)
            .collect(Collectors.toList());

        List<Long> entryIds = entries.stream()
            .map(Entry::getId)
            .collect(Collectors.toList());

        eventPublisher.publishEvent(new ActivityNotificationsSaveEvent(notificationIds, entryIds));
    }

    public Notification findKeywordNotificationByIdOrElseThrow(Long id) {
        return notificationRepository.findKeywordNotificationById(id).orElseThrow(
            () -> new GlobalException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
    }

    public ActivityNotification findActivityNotificationByIdOrElseThrow(Long id) {
        return notificationRepository.findActivityNotificationById(id).orElseThrow(
            () -> new GlobalException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
    }

    private void createDeadlineNotificationSchedule(Long shareId) {
        Share share = shareService.findByIdOrElseThrow(shareId);

        Instant instant = share.getClosedDateTime().minusMinutes(30)
            .toInstant(seoulZoneOffset);

        taskScheduler.schedule(() -> saveActivityNotification(shareId, null, ActivityType.DEADLINE),
            instant);
    }

    private void sendKeywordNotifications(List<Keyword> keywords,
        List<Notification> notifications) {

        List<Long> notificationIds = notifications.stream()
            .map(Notification::getId)
            .collect(Collectors.toList());

        List<Long> keywordIds = keywords.stream()
            .map(Keyword::getId)
            .collect(Collectors.toList());

        eventPublisher.publishEvent(new KeywordNotificationsSaveEvent(notificationIds, keywordIds));
    }

    private Notification findWithMemberByIdOrElseThrow(Long id) {
        return notificationRepository.findWithMemberById(id).orElseThrow(
            () -> new GlobalException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
    }
}
