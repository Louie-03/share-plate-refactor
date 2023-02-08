package louie.hanse.shareplate.core.share.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShareRegisterEvent {

    private final Long shareId;
    private final Long memberId;
}
