package louie.hanse.shareplate.core.notification.controller;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.common.argumentresolver.MemberVerification;
import louie.hanse.shareplate.core.notification.dto.response.ActivityNotificationResponse;
import louie.hanse.shareplate.core.notification.dto.response.KeywordNotificationResponse;
import louie.hanse.shareplate.core.notification.service.NotificationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/activity")
    public List<ActivityNotificationResponse> activityNotificationList(@MemberVerification Long memberId) {
        return notificationService.getActivityNotificationList(memberId);
    }

    @GetMapping("/notifications/keyword")
    public List<KeywordNotificationResponse> keywordNotificationList(@MemberVerification Long memberId) {
        return notificationService.getKeywordNotificationList(memberId);
    }

    @DeleteMapping("/notifications/{id}")
    public void deleteOnlyOneNotification(@PathVariable(required = false)
    @NotNull(message = "PathVariable의 notificationId가 비어있습니다.")
    @Positive(message = "알림 id는 양수여야 합니다.") Long id,
        @MemberVerification Long memberId) {
        notificationService.delete(id, memberId);
    }

    @DeleteMapping("/notifications")
    public void deleteSelectionNotification(@RequestBody Map<String, @Valid List<@Valid
        @NotNull(message = "PathVariable의 notificationId가 비어있습니다.")
        @Positive(message = "알림 id는 양수여야 합니다.") Long>> map,
        @MemberVerification Long memberId) {
        List<Long> idList = map.get("idList");
        notificationService.deleteAll(idList, memberId);
    }
}
