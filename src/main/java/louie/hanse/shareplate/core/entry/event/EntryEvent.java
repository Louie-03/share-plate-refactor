package louie.hanse.shareplate.core.entry.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EntryEvent {

    private final Long shareId;
    private final Long memberId;
}
