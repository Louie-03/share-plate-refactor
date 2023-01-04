package louie.hanse.shareplate.common.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.exception.invalid.InvalidLongitudeException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Longitude {

    private double longitude;

    public Longitude(Double longitude) {
        validateLongitude(longitude);
        this.longitude = longitude;
    }

    private void validateLongitude(Double longitude) {
        if (Objects.isNull(longitude)) {
            throw new InvalidLongitudeException();
        }
    }
}
