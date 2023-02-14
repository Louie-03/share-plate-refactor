package louie.hanse.shareplate.core.notification.event.keyword;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeywordNotificationsSaveEvent {

    private List<Long> keywordNotificationIds;
    private List<Long> keywordIds;

}
