package louie.hanse.shareplate.common.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.utils.StringUtils;
import louie.hanse.shareplate.common.exception.invalid.InvalidLocationException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Location {

    private String location;

    public Location(String location) {
        validateLocation(location);
        this.location = location;
    }

    private void validateLocation(String location) {
        if (StringUtils.isBlank(location)) {
            throw new InvalidLocationException();
        }
    }

}
