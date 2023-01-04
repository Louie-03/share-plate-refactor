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

    // TODO: KeywordLatitudeValidator의 로직을 validateLatitude 메서드에 추가, KeywordLatitudeValidator 제거
    private void validateLatitude(Double latitude) {
        if (Objects.isNull(latitude)) {
            throw new InvalidLatitudeException();
        }
    }
}
