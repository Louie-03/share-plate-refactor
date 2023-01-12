package louie.hanse.shareplate.core.share.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.exception.invalid.InvalidImageUrlException;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.share.exception.InvalidTitleException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Title {

    private String title;

    public Title(String title) {
        validateTitle(title);
        this.title = title;
    }

    private void validateTitle(String title) {
        if (StringUtils.isBlank(title)) {
            throw new InvalidTitleException();
        }
    }
}
