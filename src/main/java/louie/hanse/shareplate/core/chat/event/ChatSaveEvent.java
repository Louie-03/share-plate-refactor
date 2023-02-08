package louie.hanse.shareplate.core.chat.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatSaveEvent {

    private final Long chatId;
}
