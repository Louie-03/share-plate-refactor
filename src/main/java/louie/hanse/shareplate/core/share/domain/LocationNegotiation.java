package louie.hanse.shareplate.core.share.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.core.share.exception.InvalidLocationNegotiationException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class LocationNegotiation {

    private boolean locationNegotiation;

    public LocationNegotiation(Boolean locationNegotiation) {
        validateLocationNegotiation(locationNegotiation);
        this.locationNegotiation = locationNegotiation;
    }

    private void validateLocationNegotiation(Boolean locationNegotiation) {
        if (Objects.isNull(locationNegotiation)) {
            throw new InvalidLocationNegotiationException();
        }
    }
}
