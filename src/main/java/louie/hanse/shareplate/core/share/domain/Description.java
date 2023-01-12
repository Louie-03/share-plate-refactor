package louie.hanse.shareplate.core.share.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.core.share.exception.InvalidDescriptionException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Description {

    private String description;

    public Description(String description) {
        validateDescription(description);
        this.description = description;
    }

    private void validateDescription(String description) {
        if (StringUtils.isBlank(description)) {
            throw new InvalidDescriptionException();
        }
    }
}
