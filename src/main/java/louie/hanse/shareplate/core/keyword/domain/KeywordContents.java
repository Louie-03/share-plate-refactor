package louie.hanse.shareplate.core.keyword.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.keyword.exception.InvalidKeywordContentsException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class KeywordContents {

    private String contents;

    public KeywordContents(String contents) {
        validateContents(contents);
        this.contents = contents;
    }

    private void validateContents(String contents) {
        if (StringUtils.isBlank(contents)) {
            throw new InvalidKeywordContentsException();
        }
    }
}
