package louie.hanse.shareplate.chat.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.chat.exception.InvalidChatContentsException;
import louie.hanse.shareplate.common.utils.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ChatContents {

    private String contents;

    public ChatContents(String contents) {
        validateContents(contents);
        this.contents = contents;
    }

    private void validateContents(String contents) {
        if (StringUtils.isBlank(contents)) {
            throw new InvalidChatContentsException();
        }
    }

}
