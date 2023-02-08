package louie.hanse.shareplate.core.notification.event.keyword;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KeywordNotificationsSaveEvent {

    private final List<Long> keywordNotificationIds;
    private final List<Long> keywordIds;

}
