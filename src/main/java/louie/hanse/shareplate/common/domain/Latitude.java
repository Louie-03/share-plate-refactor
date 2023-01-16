package louie.hanse.shareplate.common.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.common.exception.invalid.InvalidLatitudeException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Latitude {

    private double latitude;

    public Latitude(Double latitude) {
        validateLatitude(latitude);
        this.latitude = latitude;
    }

    private void validateLatitude(Double latitude) {
        if (Objects.isNull(latitude) || isOutOfKoreaLatitudeRange(latitude)) {
            throw new InvalidLatitudeException();
        }
    }

    private boolean isOutOfKoreaLatitudeRange(Double latitude) {
        return 33.1 > latitude || latitude > 38.45;
    }
}
