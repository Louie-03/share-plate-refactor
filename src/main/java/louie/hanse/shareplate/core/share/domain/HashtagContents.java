package louie.hanse.shareplate.core.share.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.share.exception.InvalidHashtagContentsException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class HashtagContents {

    private String contents;

    public HashtagContents(String contents) {
        validateContents(contents);
        this.contents = contents;
    }

    private void validateContents(String contents) {
        if (StringUtils.isBlank(contents)) {
            throw new InvalidHashtagContentsException();
        }
    }
}
